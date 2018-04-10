/*LaToya Anderson - Extra Credit 2
 * CISC 1115
 * March 28, 2018
 * This program performs bank transactions and accepts and 
 * deletes accounts as requested
 */
import java.util.Scanner;
import java.io.*;

public class BankAcct {

	public static void main(String[] args)throws IOException{
		
		//Character variable for bank transactions
		char bankTrans;
		//Holds true or false to continue or end program
		boolean notDone = true;
		
		//holds total number of accounts
		int num_acct;
		//returns total number of accounts
		int maxNum=0;
		//size constant for array
		int MAX_NUM = 200;
		
		//String transaction;
		//Array for account numbers
		int[] account = new int[MAX_NUM];
		//Array for balances
		double[] balance = new double[MAX_NUM];
		
		PrintWriter outPut = new PrintWriter("/home/latoya/"
				+ "eclipse-workspace/BankAccount/"
				+ "UpdatedAccounts.txt");
		
		//Method reads in accounts and balances into array
		//and returns total number of accounts
		num_acct = readAccts(account,balance,maxNum);
		outPut.println("Account balances");
		
		//Prints initial accounts and balances list
		printArray(account,balance,num_acct,outPut);
		
		File transactions = new File("/home/latoya/eclipse"
				+ "-workspace/BankAccount/src/"
				+ "BankTransactions.txt");
		
		//Opens Scanner to read in Accounts.txt file
		Scanner inPut = new Scanner(transactions);
		
		//Calls method to prints bank transaction choices
		menu();
		
		
		/*loop initiates once transaction is chosen and 
		checks for valid input, calls method for that transaction,
		or quits program*/
		do{
			
			//User chooses bank transaction
			System.out.println("Choose from selection above: \n");
			
			//Holds char for switch method
			bankTrans = inPut.next().charAt(0);
			
			//Calls method of chosen transaction
			switch(bankTrans){
				case'D':
				case'd':
					//method adds deposits to existing bank balance
					deposit(account,balance,num_acct,inPut,outPut);
					break;
				case'W':
				case'w':
					//method withdraws funds from 
					//existing bank balance
					withdrawal(account,balance,
							num_acct,inPut,outPut);
					break;
				case'B':
				case'b':
					//method prints balance if account exists
					balance(account,balance,
							num_acct,inPut,outPut);
					break;
				case'N':
				case'n':
					//method returns new total number of accounts
					//if account doesn't already exist
					num_acct = newAcct(account,balance,
							num_acct,inPut,outPut);
					break;
				case'X':
				case'x':
					//method deletes account if it exists and 
					//has a zero balance
					num_acct = deleteAcct(account,balance,
							num_acct,inPut,outPut);
					break;
				
				case'Q':
				case'q':
					//method ends program when finished
					notDone = quit(inPut,outPut);
					System.out.println("You have quit"
							+ " this program.");
					break;
				default:
					//outPut.println();
					outPut.println(bankTrans + 
							" is an invalid entry.");
					outPut.println();
					break;	
			}
		
		
		}while(notDone);
		
		outPut.flush();
		
		//Prints final account and balances list
		printArray(account,balance,num_acct,outPut);
		
		outPut.close();;

	}
	
	/*Input: acctnum - array of account numbers
	 * 		 balance - array of balances
	 * 		 num_accts - total number of existing accounts
	 * 		 input - references BankTransactions
	 * 		 output - references UpdatedAccounts
	 * Process: Prompts user to input account number
	 * 			Calls findAcct to check if it exists
	 * 			If account exists then prompts user to withdraw
	 * 			 from initial balance
	 * 			If withdrawal amount is higher than balance,
	 * 				prints error message and doesn't 
	 * 				do withdrawal
	 * Output: Prints new balance if account exists
	 * 			Prints error message if account doesn't exist 
	 * 			Prints error message if withdrawal amount 
	 * 				is higher than account balance
	 */
	public static void withdrawal(int[] acctnum,
			double[] balance, int num_accts,
			Scanner input,PrintWriter output) {
		int acct, acctTest;
		
		//Create a Scanner object to read user input
		//Scanner keyboard = new Scanner(System.in);
		
		//Prompt user for account number
		System.out.println("Enter account number: ");
		
		//Reads in account number to test if valid
		acctTest = input.nextInt();
		
		/*if account exists then acctTest receives 
		account index number*/
		acct = findAcct(acctnum,num_accts,acctTest);
		
		output.println("Transaction: Withdrawal");
		
		if(acct != -1) {
			//Holds users deposit amount
			double withdrawal,newBalance;
			
			output.printf("Account number: %d\n",acctTest);

			output.printf("Account balance: "
					+ "$%.2f\n", balance[acct]);
		
			//Prompt user for withdrawal amount
			System.out.print("How much would you "
					+ "like to withdraw? ");
			
			//Reads in balance and withdrawal from Accounts file
			newBalance = input.nextDouble();
			withdrawal = input.nextDouble();
			
			//Withdraws amount if funds are sufficient
			if (withdrawal < newBalance && withdrawal > 0) {
				
				output.printf("Withdrawing: $%.2f\n", withdrawal);
				//equation adds deposit to bank balance
				balance[acct] = newBalance - withdrawal;
				
				output.printf("New account balance: "
						+ "$%.2f\n", balance[acct]);
			}
			//Prints message if there is a 0.00 withdrawal amount
			else if(withdrawal == 0.00) {
				output.printf("Withdrawal: %.2f\n", withdrawal);
				output.printf("Status: Invalid amount. Cannot"
						+ " withdraw $%.2f\n", withdrawal);
			}
			/*Prints error message for attempting
			to withdraw negative balance*/
			else if(withdrawal < 0) {
				output.printf("Withdrawal: $%.2f\n", withdrawal);
				output.println("Status: Invalid amount. "
						+ "Cannot make a negative withdrawal");
			}
			/*Prints error message to file
			for withdrawing more than current balance*/
			else {
				output.printf("Withdrawal: $%.2f\n", withdrawal);
				output.println("Status: Insufficient funds");
				}
			System.out.println();
		}
		
		//Prints error message for invalid accounts
		else {
			output.println("Account: " + acctTest);
			output.println("Status: Does not exist.");
		}
		output.println();
		output.flush();

	}
	
	/*Input: acctnum - array of account numbers
	 * 		 balance - array of balances
	 * 		 num_accts - total number of existing accounts
	 * 		 input - references BankTransactions
	 * 		 output - references UpdatedAccounts
	 * Process: Prompts user to input account number
	 * 			Calls findAcct to check if it exists
	 * 			If it exists then prompts user to add
	 * 				deposit which is added to initial balance
	 * 			If it doesn't exist, prints error message
	 * Output: Prints new balance if account exists
	 * 		  Prints error message if account doesn't exist
	 */
	public static void deposit(int[] acctnum,double[] balance, 
			int num_accts, Scanner input, PrintWriter output) {

		int acct, acctTest;
		
		//Prompt user for account number
		System.out.println("Enter account number: ");
		acctTest = input.nextInt();
		
		/*if account exists then acctTest 
		receives account index number*/
		acct = findAcct(acctnum,num_accts,acctTest);
		
		output.println("Transaction: Deposit");
		
		//Adds deposit if return value is index number
		if(acct != -1) {
			//Holds users deposit amount
			double deposit, newBalance, bal;
			
			//Reads in deposit from BankTransactions file
			bal = input.nextDouble();
			deposit = input.nextDouble();
			
			//Prints account number
			output.printf("Account number: %d\n",acctTest);
			
			//Prints account current account balance
			output.printf("Account balance $%.2f\n",bal);
			
			//Prompt user for deposit amount
			System.out.println("How much would you "
					+ "like to deposit? ");
			
			//Tests if deposit amount is more than $0.00
			if(deposit > 0.00) {
				//Prints deposit to Updated Accounts file
				output.printf("Deposit: $%.2f\n", deposit);

				//equation adds deposit to bank balance
				balance[acct] = bal + deposit;
					
				output.printf("New account "
					+ "balance: $%.2f\n", balance[acct]);
			}
			//Tests if deposit is zero dollars
			else if (deposit == 0.00) {
				output.printf("Deposit: $%.2f\n", deposit);
				output.printf("Status: Invalid deposit."
						+ "Cannot deposit $%.2f\n", deposit);
			}
			//Executes if deposit is a negative amount
			else {
				output.printf("Deposit: $%.2f\n", deposit);
				output.println("Status: Invalid deposit amount");
			}
		}
		
		//Prints to file if account does not exist
		else {
			output.println("Account: " + acctTest);
			output.println("Status: Does not exist."); 
		}
		
		output.println();
		output.flush();
	}
	
	
	/*Input:acctnum - array of account numbers
	 * 		balance - array of balances
	 * 		num_accts - total number of existing accounts
	 * 		input - references BankTransactions
	 * 		output - references UpdatedAccounts
	 * Process: Prompts user for new account number
	 * 			Calls findAcct to check if it exists
	 * 			If account doesn't exists then adds to 
	 * acctnum array and zero balance to balance array
	 * 			If account exists, prints error message
	 * Output: Returns updated number of accounts to main
	 * 			Prints error message if account does exists
	 */	
	public static int newAcct(int[] acctnum, double[] balance,
			int num_accts,Scanner input,PrintWriter output) {
		
		//Holds new account number and i
		int acct, acctTest;
		int index=0, newNum = 0;
		//Holds input balance
		double newBalance = 0.00;
		
		//Create a Scanner object to read user input
		//Scanner keyboard = new Scanner(System.in);
	
		//Prompt user for new account number
		System.out.println("Enter new account number: ");
		acctTest = input.nextInt();
		
		/*if account exists then acctTest 
		receives account index number*/
		acct = findAcct(acctnum,num_accts,acctTest);
		output.println("Transaction: New account");
		
		//Prints error message if account exists
		if(acct != -1) {
			output.printf("Account number: %d\n", acctTest);
			output.printf("Account balance: %.2f\n",balance[acct]);
			output.println("Status: Error. Account "
					+ "already exists.\n");
			newNum = num_accts;
		}
		else {
				output.printf("Account number: %d\n", acctTest);
				output.printf("New account balance: "
						+ "$%.2f\n", newBalance);
				output.println("Status: New account created");
				
				acctnum[num_accts] = acctTest;
				balance[num_accts] = newBalance;
				output.println();
				
				//num_accts =+1;
				for(index = 0; index < num_accts+1; index++) {
					System.out.println(acctnum[index] + 
							" " + balance[index]);	
			}
				
		//Holds new total number of accounts		
		num_accts = index;
		}
			
		System.out.println();
		output.flush();
		return num_accts;
	}
	
	/*Input: acctnum - array of account numbers
	 * 		 balance - array of balances
	 * 		 num_accts - total number of existing accounts
	 * 		 input - references BankTransactions
	 * 		 output - references UpdatedAccounts
	 * Process: Prompts user to input account number
	 * 			Calls findAcct to check if it exists
	 * 			If acccount exists then prints balance
	 * 			If account doesn't exist, prints error message
	 * Output: Prints balance if account exists
	 * 			Prints error message if account doesn't exist 
	 */
	public static void balance(int[] acctnum,double[] balance,
			int num_accts,Scanner input,PrintWriter output){
		int acct, acctTest;
		double bal;
		
		//Create a Scanner object to read user input
		//Scanner keyboard = new Scanner(System.in);
	
		//Prompt user for account number
		System.out.println("Enter account number: ");
		
		acctTest = input.nextInt();
		
		output.println("Transaction Type: Balance");
		//if account exists then acctTest 
		//receives account index number
		acct = findAcct(acctnum,num_accts,acctTest);
	
		if(acct != -1) {	
			bal = input.nextDouble();
			output.printf("Account number %d ",acctnum[acct]);
			output.println();
			output.printf("Balance is: $%.2f\n", balance[acct]);
			
			
		}
		else {
			
			output.println("Account: " + acctTest);
			output.println("Status: Does not exist");
			}
		output.println();
		output.flush();
		
	}	
	
	/*Input - input - scanner object from main of
						BankTransactions file
	 * 		  output - scanner object from main for 
	 * 					Updated Accounts file
	 * Process - 	Prints end of transaction status to file
	 * 				Prints title "Final Transactions"
	 *Output - Returns false to end program
	 */
	public static boolean quit(Scanner input, PrintWriter output)
	{
		output.println("\nTransactions finished\n");
		output.println("Final account balances");
		return false;
	}
	
	
	/*Input: acctnum - array of account numbers
	 * 		 balance - array of balances
	 * 		 num_accts - total number of existing accounts
	 * 		 input - references BankTransactions
	 * 		 output - references UpdatedAccounts
	 * Process: Prompts user to input account number
	 * 			Calls findAcct to check if it exists
	 * 			If account exists then prints balance
	 * 			If account doesn't exist, prints error message
	 * Output: Prints balance if account exists
	 * 			Prints error message if account doesn't exist 
	 */
	public static int deleteAcct(int[] acctnum, double[] balance,
			int num_accts,Scanner input,PrintWriter output) {
	
		//Receives return value of index or -1
		int acct;
		//Increments for return index
		int newNum=0,index = 0;
		//Holds read in account number for validity test
		int acctTest = 0;
		//Holds read in balance
		double bal=0.00;	
				
		//Prompt user for account number
		System.out.println("Enter account number: ");
		acctTest = input.nextInt();
		bal = input.nextDouble();
		
		//if account exists then acctTest receives 
		//account index number
		acct = findAcct(acctnum,num_accts,acctTest);
		
		output.println("Transaction: Remove account");
		
		//Prints error message if account doesn't exist
		if(acct == -1) {
			System.out.println("If acct == -1:" + acct);
			output.printf("Account number: %d\n", acctTest);
			output.println("Status: Account does not exist "
					+ "and cannot be deleted.");
			newNum = num_accts;
		}
		
		//Prints error message if account exists and has a balance
		else if(acct > 0 && bal != 0.00) {
			System.out.println("If acct > 0: " + acct);
			output.printf("Account number: %d\n", acctTest);
			output.printf("Status: Account exists, has a "
					+ "balance and cannot be deleted.");
			output.printf("Account balance: %.2f\n", balance[acct]);
			newNum = num_accts;
		}
		
		/*Removes account with zero balance and and assigns 
		 * updated number of accounts to num_accts*/
		else if(acct > 0 && bal == 0.00){
			
			System.out.println();
			output.printf("Account number: %d\n", acctTest);
			output.printf("Account balance: %.2f\n", balance[acct]);
			output.printf("Status: Account exists. Closed.");
			
			//Reorders accounts and balances 
			//in array to remove acctTest and its balance
			for(index = acct; index < num_accts; index++) {
				
				//inserts data from next highest 
				//index to previous index
				acctnum[index] = acctnum[acct+1];
				balance[index] = balance[acct+1];
				
				acct++;

				newNum = index;
				}
			
			output.println();
		}	
		
		output.println();
		output.flush();
		return newNum;		
		
	}	
	
	/*Input: accnt - an uninitiated array to be 
	 * 				filled with account numbers
	 * 		balance - an uninitiated array to be
	 * 				 filled with account balance
	 * 		max_accts - an uninitiated datatype int
	 * 				to return max number of existing 
	 * 				accounts
	 * Process: reads account numbers and balance into
	 * 			parallel arrays and determines their 
	 * 			total number
	 * Output: two parallel arrays filled with 
	 * 			account information, their balances and
	 * 			returns max_accts - total number of accounts
	 * 			
	 */	
	public static int readAccts(int[] accnt,
			double[] balance, int max_accts)throws IOException
	{
		int index = 0;
		
		/*reads in accounts into accnt array and balances 
		into balance array*/
		File myFile = new File("/home/latoya/eclipse-"
				+ "workspace/BankAccount/Accounts.txt");
		
		//Creates scanner object to read the input file
		Scanner inputFile = new Scanner(myFile);
		
			//fills acct and balance arrays read in from myFile
			while(inputFile.hasNext()){

				accnt[index] = inputFile.nextInt();
				balance[index] = inputFile.nextDouble();
				
				index++;
			}

		inputFile.close();
		return index;
	}
	
	/*Input: no arguments
	 * Process: prints bank 
	 * 			transaction menu
	 * Output: prints bank
	 * 			transaction menu
	 */
	public static void menu() {
		System.out.println("W - Withdrawal");
		System.out.println("D - Deposit");
		System.out.println("N - New account");
		System.out.println("B - Balance");
		System.out.println("Q - Quit");
		System.out.println("X - Delete Account");
		
	}
	
	/*Input: acctnum - points to address of array account numbers
	 * 		num_accts - total number of existing accounts
	 * 		account - receives acctTest and returns index
	 * 					if it exists
	 * Process: Compares received account number to acctnum array
	 * Output: Returns acctnum array index if 
	 * 			valid or -1 if invalid
	 * 
	 */
	public static int findAcct(int[] acctnum, int num_accts,
			int account) {
		//Holds index value if account exists and -1 if 
		//account doesn't exist
		int acctExist=0;
	
		//Iterates through array to check whether account exists
		for(int index = 0;index < acctnum.length; index++) {
			
			//Tests if account exists
			if(account == acctnum[index]) {
				acctExist = index;
				break;
			}
			//Tests if account doesn't exist
			else if(account != acctnum[index])
				acctExist = -1;
		}
		
		return acctExist;

	}
	

	/*Input: acctnum - array of account numbers
	 * 		balance - array of balances
	 * 		num_accts - total number of existing accounts
	 * 		output - Scanner object to print initial 
	 * 					and final accounts and their balances
	 * Process: Prints all accounts and their 
	 * balances including new accounts
	 * Output: Prints all accounts and their 
	 * balances including new accounts
	 */
	public static void printArray(int[] acctnum, double[] balance,
			int num_accts, PrintWriter output) throws IOException{
		

		//Prints entire array of accounts and their balances
		for(int index = 0; index < num_accts; index++) {	
			output.printf("%d %.2f\n",acctnum[index]
					, balance[index]);	
			}
		output.println();
		output.flush();

	}


}

