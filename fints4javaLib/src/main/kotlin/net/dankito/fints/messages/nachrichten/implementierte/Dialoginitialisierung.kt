package net.dankito.fints.messages.nachrichten.implementierte

import net.dankito.fints.messages.datenelemente.implementierte.Dialogsprache
import net.dankito.fints.messages.datenelemente.implementierte.KundensystemStatusWerte
import net.dankito.fints.messages.datenelemente.implementierte.Nachrichtennummer.Companion.FirstMessageNumber
import net.dankito.fints.messages.nachrichten.Nachricht
import net.dankito.fints.messages.segmente.implementierte.IdentifikationsSegment
import net.dankito.fints.messages.segmente.implementierte.Nachrichtenabschluss
import net.dankito.fints.messages.segmente.implementierte.Nachrichtenkopf
import net.dankito.fints.messages.segmente.implementierte.Verarbeitungsvorbereitung


open class Dialoginitialisierung(
    messageSize: Int, // TODO: how to get / calculate size? (give each Segment, Dataelement, ... a size value?)
    bankCountryCode: Int,
    bankCode: String,
    customerId: String,
    customerSystemId: String,
    bpdVersion: Int,
    updVersion: Int,
    language: Dialogsprache,
    productName: String,
    productVersion: String
)
    : Nachricht(listOf(
        Nachrichtenkopf(1, messageSize, "0", FirstMessageNumber),
        IdentifikationsSegment(2, bankCountryCode, bankCode, customerId, customerSystemId, KundensystemStatusWerte.NichtBenoetigt), // TODO: KundensystemStatusWerte
        Verarbeitungsvorbereitung(3, bpdVersion, updVersion, language, productName, productVersion),
        Nachrichtenabschluss(4, FirstMessageNumber)
)) {

    /**
     * Zur Einleitung des Prozesses der Gewährleistung einer starken Kun-
    denauthentifizierung gemäß [PSD2] muss bei TAN-Verfahren ein HKTAN-
    Segment ab Segmentversion #6 eingestellt werden, wenn ein Kreditinstitut
    die Verwendung von HKTAN #6 unterstützt (BPD). Wenn HKTAN  #6
    nicht gesendet wird, kann der Dialog vom Institut mit dem Rückmeldungs-
    code 9075 – Dialog abgebrochen - Starke Authentifizierung
    erforderlich abgewiesen werden.

     */

}