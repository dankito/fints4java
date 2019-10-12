package net.dankito.fints.messages.datenelemente.implementierte.encryption

import net.dankito.fints.messages.Existenzstatus
import net.dankito.fints.messages.datenelemente.basisformate.BinaerDatenelement


/**
 * For PIN/TAN data doesn't get encrypted at all.
 *
 * It simply gets, prefixed by '@<payload_length>@', appended to VerschluesselteDaten segment header
 */
open class PinTanVerschluesselteDatenDatenelement(payload: String)
    : BinaerDatenelement(payload, Existenzstatus.Mandatory)