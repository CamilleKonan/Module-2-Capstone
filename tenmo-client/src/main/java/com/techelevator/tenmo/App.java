package com.techelevator.tenmo;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.TransferService;
import java.math.BigDecimal;
import java.util.List;

public class App {
    private final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }
    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }
    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        } else {
            accountService.setAuthToken(currentUser.getToken());
            transferService.setAuthToken(currentUser.getToken());
        }
    }
    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }
    private void viewCurrentBalance() {
        BigDecimal balance = accountService.getBalance(currentUser.getUser().getId());
        consoleService.printBalance(balance);
    }
    private void viewTransferHistory() {
        List<TransferDto> transfers = transferService.getTransfersByUserId(currentUser.getUser().getId());
        consoleService.printTransferHistory(transfers);
        int transferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
        if (transferId != 0) {
            TransferDto transfer = transferService.getTransferById(transferId);
            consoleService.printTransferDetails(transfer);
        }
    }
    private void viewPendingRequests() {
        List<TransferDto> pendingTransfers = transferService.getPendingTransfers(currentUser.getUser().getId());
        consoleService.printPendingTransfers(pendingTransfers);
        int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
        if (transferId != 0) {
            String choice = consoleService.promptForString("Approve or Reject? (A/R): ").trim().toUpperCase();
            if ("A".equals(choice)) {
                transferService.approveTransfer(transferId, currentUser.getUser().getId());
            } else if ("R".equals(choice)) {
                transferService.rejectTransfer(transferId, currentUser.getUser().getId());
            }
        }
    }
    private void sendBucks() {
        int recipientId = consoleService.promptForInt("Enter ID of user you are sending to: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to send: ");
        boolean success = accountService.transferBalance(currentUser.getUser().getId(), recipientId, amount);
        if (success) {
            System.out.println("Transfer successful!");
        } else {
            System.out.println("Transfer failed. Please check your balance and try again.");
        }
    }
    private void requestBucks() {
        int recipientId = consoleService.promptForInt("Enter ID of user you are requesting from: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Enter amount to request: ");
        TransferDto transferRequest = new TransferDto();
        transferRequest.setAccountFrom(recipientId);
        transferRequest.setAccountTo(currentUser.getUser().getId());
        transferRequest.setAmount(amount);
        transferRequest.setTransferTypeId(1); // Assuming '1' indicates a "Request" transfer
        transferRequest.setTransferStatusId(1); // Assuming '1' indicates "Pending"
        boolean success = transferService.createTransfer(transferRequest);
        if (success) {
            System.out.println("Request sent!");
        } else {
            System.out.println("Request failed. Please try again.");
        }
    }
}