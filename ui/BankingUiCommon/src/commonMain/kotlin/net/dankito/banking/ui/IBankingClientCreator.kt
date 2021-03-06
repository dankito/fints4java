package net.dankito.banking.ui

import net.dankito.utils.multiplatform.File
import net.dankito.banking.ui.model.TypedBankData
import net.dankito.banking.util.IAsyncRunner


interface IBankingClientCreator {

    fun createClient(
        bank: TypedBankData,
        dataFolder: File,
        asyncRunner: IAsyncRunner,
        callback: BankingClientCallback
    ): IBankingClient

}