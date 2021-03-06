import SwiftUI


struct UIKitTextField: UIViewRepresentable {
    
    static private var NextTagId = 234567 // start at a high, very unlikely number to not interfere with manually set tags

    
    @Binding private var text: String
    
    private var placeholder: String
    
    private var keyboardType: UIKeyboardType = .default
    private var autocapitalizationType: UITextAutocapitalizationType = .sentences
    private var addDoneButton: Bool = false
    
    private var isPasswordField: Bool = false
    
    @State private var focusOnStart = false
    private var focusNextTextFieldOnReturnKeyPress = false
    @Binding private var focusTextField: Bool
    
    private var isFocusedChanged: ((Bool) -> Void)? = nil
    
    private var textAlignment: NSTextAlignment = .natural
    private var isUserInputEnabled: Bool = true
    
    private var actionOnReturnKeyPress: (() -> Bool)? = nil
    
    private var textChanged: ((String) -> Void)? = nil

    
    init(_ titleKey: String, text: Binding<String>, keyboardType: UIKeyboardType = .default, autocapitalizationType: UITextAutocapitalizationType = .sentences, addDoneButton: Bool = false,
         isPasswordField: Bool = false, focusOnStart: Bool = false, focusNextTextFieldOnReturnKeyPress: Bool = false, focusTextField: Binding<Bool> = .constant(false),
         isFocusedChanged: ((Bool) -> Void)? = nil,
         textAlignment: NSTextAlignment = .natural, isUserInputEnabled: Bool = true,
         actionOnReturnKeyPress: (() -> Bool)? = nil, textChanged: ((String) -> Void)? = nil) {
        
        self.placeholder = titleKey
        _text = text
        
        self.keyboardType = keyboardType
        self.autocapitalizationType = autocapitalizationType
        self.addDoneButton = addDoneButton
        
        self.isPasswordField = isPasswordField
        
        self._focusOnStart = State(initialValue: focusOnStart)
        self.focusNextTextFieldOnReturnKeyPress = focusNextTextFieldOnReturnKeyPress
        self._focusTextField = focusTextField
        self.isFocusedChanged = isFocusedChanged
        
        self.textAlignment = textAlignment
        self.isUserInputEnabled = isUserInputEnabled
        
        self.actionOnReturnKeyPress = actionOnReturnKeyPress
        self.textChanged = textChanged
    }
    

    func makeUIView(context: UIViewRepresentableContext<UIKitTextField>) -> UITextField {
        let textField = UITextField(frame: .zero)
        
        textField.placeholder = placeholder.localize()
        
        textField.isSecureTextEntry = isPasswordField
        
        textField.keyboardType = keyboardType
        textField.autocapitalizationType = autocapitalizationType
        
        if addDoneButton {
            addDoneButtonToKeyboard(textField, context.coordinator)
        }
        
        textField.delegate = context.coordinator
        
        if isPasswordField {
            addTogglePasswordVisibilityButton(textField, context.coordinator)
        }
        
        // set tag on all TextFields to be able to focus next view (= next tag). See Coordinator for more details
        Self.NextTagId = Self.NextTagId + 1 // unbelievable, there's no ++ operator
        textField.tag = Self.NextTagId
        
        textField.textAlignment = textAlignment
        
        return textField
    }

    func updateUIView(_ uiView: UITextField, context: UIViewRepresentableContext<UIKitTextField>) {
        uiView.text = text

        if focusOnStart {
        // on iOS 14 calling .focus() in makeUIView() doesn't work -> do it here and reset focusOnStart property
            DispatchQueue.main.async {
                focusOnStart = false
            }
            uiView.focus()
        }

        if focusTextField {
            DispatchQueue.main.async { // in very few cases focusTextField gets called during view update resulting in 'undefined behavior' -> async() fixes this
                uiView.focus()

                DispatchQueue.main.async {
                    self.focusTextField = false // reset value so that it can be set again (otherwise it may never gets resetted and then updateUIView() requests focus even though already another view got the focus in the meantime)
                }
            }
        }
    }
    
    private func addTogglePasswordVisibilityButton(_ textField: UITextField, _ coordinator: Coordinator) {
        coordinator.textField = textField
        
        let togglePasswordVisiblityView = UIButton()
        togglePasswordVisiblityView.setImage(UIImage(systemName: "eye.slash.fill"), for: .normal)
        togglePasswordVisiblityView.tintColor = UIColor.secondaryLabel
        togglePasswordVisiblityView.addTarget(coordinator, action:#selector(coordinator.togglePasswordVisibility), for: .touchUpInside)
        
        textField.leftView = togglePasswordVisiblityView
        textField.leftViewMode = .always
    }
    
    private func addDoneButtonToKeyboard(_ textField: UITextField, _ coordinator: Coordinator) {
        coordinator.textField = textField
        
        let spacer = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let doneButton = UIBarButtonItem(barButtonSystemItem: .done, target: coordinator, action: #selector(coordinator.doneButtonTapped))

        let doneToolbar: UIToolbar = UIToolbar(frame: CGRect.init(x: 0, y: 0, width: UIScreen.main.bounds.width, height: 50))
        doneToolbar.barStyle = .default

        doneToolbar.items = [spacer, doneButton]
        doneToolbar.sizeToFit()

        textField.inputAccessoryView = doneToolbar
    }
    

    func makeCoordinator() -> UIKitTextField.Coordinator {
        return Coordinator(text: $text, focusNextTextFieldOnReturnKeyPress: focusNextTextFieldOnReturnKeyPress, isFocusedChanged: isFocusedChanged,
                           isUserInputEnabled: isUserInputEnabled, actionOnReturnKeyPress: actionOnReturnKeyPress, textChanged: textChanged)
    }


     class Coordinator: NSObject, UITextFieldDelegate {

        @Binding private var text: String
        
        private var focusNextTextFieldOnReturnKeyPress: Bool
        
        private var isFocusedChanged: ((Bool) -> Void)? = nil
        
        private var isUserInputEnabled: Bool
        
        private var actionOnReturnKeyPress: (() -> Bool)?
        
        private var textChanged: ((String) -> Void)?
        
        var textField: UITextField? = nil


        init(text: Binding<String>, focusNextTextFieldOnReturnKeyPress: Bool, isFocusedChanged: ((Bool) -> Void)? = nil, isUserInputEnabled: Bool,
             actionOnReturnKeyPress: (() -> Bool)? = nil, textChanged: ((String) -> Void)? = nil) {
            _text = text
            
            self.focusNextTextFieldOnReturnKeyPress = focusNextTextFieldOnReturnKeyPress
            self.isFocusedChanged = isFocusedChanged
            self.isUserInputEnabled = isUserInputEnabled
            
            self.actionOnReturnKeyPress = actionOnReturnKeyPress
            
            self.textChanged = textChanged
        }
        
        
        func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
            if isUserInputEnabled {
                if textField.isFirstResponder {
                    isFocusedChanged?(true)
                }
            }
            
            return isUserInputEnabled
        }
        
        func textFieldDidEndEditing(_ textField: UITextField, reason: UITextField.DidEndEditingReason) {
            isFocusedChanged?(false)
        }

        func textFieldDidChangeSelection(_ textField: UITextField) {
            let newText = textField.text ?? ""
            let didTextChange = newText != text // e.g. if just the cursor has been placed to another position then textFieldDidChangeSelection() gets called but text didn't change

            DispatchQueue.main.async { // to not update state during view update
                self.text = newText

                if didTextChange {
                    self.textChanged?(newText)
                }
            }
        }
        
        func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            return handleReturnKeyPress(textField)
        }
        
        @discardableResult
        func handleReturnKeyPress(_ textField: UITextField) -> Bool {
            var didHandleReturnKey = actionOnReturnKeyPress?() ?? false
            
            if didHandleReturnKey == false && focusNextTextFieldOnReturnKeyPress == true {
                let nextViewTag = textField.tag + 1
                
                let nextView = textField.superview?.superview?.superview?.viewWithTag(nextViewTag)
                    ?? textField.superview?.superview?.superview?.superview?.superview?.viewWithTag(nextViewTag) // for text fields in Lists (tables)
                    ?? textField.superview?.superview?.superview?.viewWithTag(nextViewTag + 1) // iOS 14 often creates the same TextField twice but displays it once -> to select next view use nextViewTag + 1
                    ?? textField.superview?.superview?.superview?.superview?.superview?.viewWithTag(nextViewTag + 1)
                
                didHandleReturnKey = nextView?.focus() ?? false
            }

            if didHandleReturnKey == false {
                textField.clearFocus() // default behaviour
            }
            
            return didHandleReturnKey
        }
        
        @objc
        func doneButtonTapped() {
            if let textField = self.textField {
                handleReturnKeyPress(textField)
            }
        }
        
        @objc
        func togglePasswordVisibility() {
            if let textField = self.textField {
                textField.isSecureTextEntry.toggle()
                
                if let togglePasswordVisiblityButton = textField.leftView as? UIButton {
                    if textField.isSecureTextEntry {
                        togglePasswordVisiblityButton.setImage(UIImage(systemName: "eye.slash.fill"), for: .normal)
                    }
                    else {
                        togglePasswordVisiblityButton.setImage(UIImage(systemName: "eye.fill"), for: .normal)
                    }
                }
            }
        }

    }
    
}


struct UIKitTextView_Previews: PreviewProvider {

    @State static private var text = ""
    
    static var previews: some View {
        UIKitTextField("Test label", text: $text)
    }

}
