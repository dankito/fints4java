import SwiftUI
import BankingUiSwift


struct BankAccountSettingsDialog: View {
    
    @Environment(\.presentationMode) var presentation
    
    @Inject private var presenter: BankingPresenterSwift
    
    
    private let account: BankAccount
    
    @State private var displayName: String
    
    @State private var unsavedChangesMessage: Message? = nil
    
    
    private var hasUnsavedData: Bool {
        return account.displayName != displayName
    }
    
    
    init(_ account: BankAccount) {
        self.account = account
        
        _displayName = State(initialValue: account.displayName)
    }
    

    var body: some View {
        Form {
            Section {
                LabelledUIKitTextField(label: "Name", text: $displayName)
            }
            
            Section {
                LabelledUIKitTextField(label: "Account holder name", value: account.accountHolderName) // TODO: senseful?
                
                LabelledUIKitTextField(label: "Bank account identifier", value: account.identifier)
                
                account.subAccountNumber.map { subAccountNumber in
                    LabelledUIKitTextField(label: "Sub account number", value: subAccountNumber)
                }
                
                account.iban.map { iban in
                    LabelledUIKitTextField(label: "IBAN", value: iban)
                }
                
                LabelledUIKitTextField(label: "Bank account type", value: account.type.name) // TODO: senseful?
            }
            
            Section(header: Text("Supports")) {
                CheckmarkListItem("Supports Retrieving Balance", account.supportsRetrievingBalance)
                
                CheckmarkListItem("Supports Retrieving Account Transactions", account.supportsRetrievingAccountTransactions)
                
                CheckmarkListItem("Supports Transferring Money", account.supportsTransferringMoney)
                
                CheckmarkListItem("Supports Instant payment transfer", account.supportsInstantPaymentMoneyTransfer)
            }
        }
        .alert(item: $unsavedChangesMessage) { message in
            Alert(title: message.title, message: message.message, primaryButton: message.primaryButton, secondaryButton: message.secondaryButton!)
        }
        .showNavigationBarTitle(LocalizedStringKey(account.displayName))
        .setCancelAndDoneNavigationBarButtons(onCancelPressed: cancelPressed, onDonePressed: donePressed)
    }
    
    
    private func cancelPressed() {
        if hasUnsavedData {
            self.unsavedChangesMessage = Message(title: Text("Unsaved changes"), message: Text("Changed data hasn't been saved. Are you sure you want to discard them?"), primaryButton: .ok(closeDialog), secondaryButton: .cancel())
        }
        else {
            closeDialog()
        }
    }
    
    private func donePressed() {
        if hasUnsavedData {
            account.userSetDisplayName = displayName

            presenter.accountUpdated(account: account.customer)
        }
        
        closeDialog()
    }
    
    private func closeDialog() {
        presentation.wrappedValue.dismiss()
    }

}


struct BankAccountSettingsDialog_Previews: PreviewProvider {

    static var previews: some View {
        BankAccountSettingsDialog(previewBanks[0].accounts[0])
    }

}