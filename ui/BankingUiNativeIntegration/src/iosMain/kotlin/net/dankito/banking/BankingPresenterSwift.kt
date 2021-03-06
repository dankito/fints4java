package net.dankito.banking

import net.dankito.banking.bankfinder.InMemoryBankFinder
import net.dankito.banking.fints.webclient.IWebClient
import net.dankito.banking.persistence.IBankingPersistence
import net.dankito.banking.search.ITransactionPartySearcher
import net.dankito.banking.ui.IRouter
import net.dankito.banking.ui.model.mapper.DefaultModelCreator
import net.dankito.banking.ui.presenter.BankingPresenter
import net.dankito.banking.ui.util.CurrencyInfoProvider
import net.dankito.banking.util.*
import net.dankito.banking.util.extraction.NoOpInvoiceDataExtractor
import net.dankito.banking.util.extraction.NoOpTextExtractorRegistry
import net.dankito.utils.multiplatform.File


class BankingPresenterSwift(dataFolder: File, router: IRouter, webClient: IWebClient, persistence: IBankingPersistence,
                            transactionPartySearcher: ITransactionPartySearcher, bankIconFinder: IBankIconFinder, serializer: ISerializer, asyncRunner: IAsyncRunner)
    : BankingPresenter(fints4kBankingClientCreator(DefaultModelCreator(), serializer, webClient), InMemoryBankFinder(), dataFolder, persistence, router, DefaultModelCreator(),
    transactionPartySearcher, bankIconFinder, NoOpTextExtractorRegistry(), NoOpInvoiceDataExtractor(), CurrencyInfoProvider(), asyncRunner) {

}