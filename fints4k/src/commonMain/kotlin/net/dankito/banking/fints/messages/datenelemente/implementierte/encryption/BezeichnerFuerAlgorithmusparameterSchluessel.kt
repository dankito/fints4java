package net.dankito.banking.fints.messages.datenelemente.implementierte.encryption

import net.dankito.banking.fints.messages.datenelemente.implementierte.ICodeEnum


enum class BezeichnerFuerAlgorithmusparameterSchluessel(override val code: String) : ICodeEnum {

    /**
     * nicht zugelassen
     */
    SymmetrischerSchluessel("5"),
    
    SymmetrischerSchluessel_VerschluesseltMitOeffentlichenSchluessel("6")

}