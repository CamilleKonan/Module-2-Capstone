package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.TransferDto;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printBalance(BigDecimal balance) {
        System.out.println("Your current balance is: $" + balance);
    }

    public void printTransferHistory(List<TransferDto> transfers)  {
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID          From/To                 Amount");
        System.out.println("-------------------------------------------");
        for (TransferDto transfer : transfers) {
            String fromTo;
            if (transfer.getAccountFrom() == transfer.getAccountTo()) {
                fromTo = "Self";
            } else if (transfer.getAccountFrom() == transfer.getAccountTo()) {
                fromTo = "Self";
            } else {
                // Assumed logic for determining if it's "From" or "To"
                fromTo = (transfer.getTransferTypeId() == 2 ? "To:    " : "From:  ") + transfer.getAccountTo();
            }
            System.out.printf("%-12d %-22s $%10.2f%n",
                    transfer.getTransferId(),
                    fromTo,
                    transfer.getAmount());
        }
        System.out.println("-------------------------------------------");
    }
    public void printTransferDetails(TransferDto transfer) {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.printf("Id: %d%n", transfer.getTransferId());
        System.out.printf("From: %d%n", transfer.getAccountFrom());
        System.out.printf("To: %d%n", transfer.getAccountTo());
        System.out.printf("Type: %s%n", getTransferTypeDescription(transfer.getTransferTypeId()));
        System.out.printf("Status: %s%n", getTransferStatusDescription(transfer.getTransferStatusId()));
        System.out.printf("Amount: $%.2f%n", transfer.getAmount());
        System.out.println("--------------------------------------------");
    }
    private String getTransferTypeDescription(int transferTypeId) {
        switch (transferTypeId) {
            case 1:
                return "Request";
            case 2:
                return "Send";
            default:
                return "Unknown";
        }
    }
    private String getTransferStatusDescription(int transferStatusId) {
        switch (transferStatusId) {
            case 1:
                return "Pending";
            case 2:
                return "Approved";
            case 3:
                return "Rejected";
            default:
                return "Unknown";
        }
    }
    public void printPendingTransfers(List<TransferDto> pendingTransfers) {
        System.out.println("-------------------------------------------");
        System.out.println("Pending Transfers");
        System.out.println("ID          To/From                Amount");
        System.out.println("-------------------------------------------");
        for (TransferDto transfer : pendingTransfers) {
            String toFrom;
            // Assuming '1' is the "Request" type; you can modify this as needed
            if (transfer.getTransferTypeId() == 1) {
                toFrom = "From:  " + transfer.getAccountFrom();
                // Transfer request coming from another user
            } else {
                toFrom = "To:    " + transfer.getAccountTo(); // Transfer request sent to another user
            }
            System.out.printf("%-12d %-22s $%10.2f%n",
                    transfer.getTransferId(),
                    toFrom,
                    transfer.getAmount());
        }
        System.out.println("-------------------------------------------");
    }
}
