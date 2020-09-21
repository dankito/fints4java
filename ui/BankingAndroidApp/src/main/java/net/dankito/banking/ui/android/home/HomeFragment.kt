package net.dankito.banking.ui.android.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import net.dankito.banking.ui.android.R
import net.dankito.banking.ui.android.adapter.AccountTransactionAdapter
import net.dankito.banking.ui.android.di.BankingComponent
import net.dankito.banking.ui.android.extensions.addHorizontalItemDivider
import net.dankito.banking.ui.android.extensions.showAmount
import net.dankito.banking.ui.model.TransactionsRetrievalState
import net.dankito.banking.ui.model.TypedBankAccount
import net.dankito.banking.ui.model.parameters.TransferMoneyData
import net.dankito.banking.ui.model.responses.GetTransactionsResponse
import net.dankito.banking.ui.presenter.BankingPresenter
import net.dankito.utils.android.extensions.asActivity
import net.dankito.utils.android.extensions.getDimension
import net.dankito.utils.multiplatform.sum
import javax.inject.Inject


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var mnitmBalance: MenuItem

    private lateinit var mnitmSearchTransactions: MenuItem

    private lateinit var mnitmUpdateTransactions: MenuItem


    private var accountsForWhichNotAllTransactionsHaveBeenFetched = listOf<TypedBankAccount>()

    private var doNotShowFetchAllTransactionsOverlay = false


    private val transactionAdapter: AccountTransactionAdapter

    protected var appliedTransactionsFilter = ""


    @Inject
    protected lateinit var presenter: BankingPresenter


    init {
        BankingComponent.component.inject(this)

        transactionAdapter = AccountTransactionAdapter(presenter)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(this, Observer {
//            textView.text = it
//        })


        val rcyvwAccountTransactions: RecyclerView = rootView.findViewById(R.id.rcyvwAccountTransactions)
        rcyvwAccountTransactions.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcyvwAccountTransactions.adapter = transactionAdapter
        rcyvwAccountTransactions.addHorizontalItemDivider()
        rcyvwAccountTransactions.isNestedScrollingEnabled = false

        registerForContextMenu(rcyvwAccountTransactions) // this is actually bad, splits code as context menu is created in AccountTransactionAdapter

        rootView.btnFetchAllTransactions.setOnClickListener {
            fetchAllTransactions()
        }

        rootView.btnHideFetchAllTransactionsOverlay.setOnClickListener {
            doNotShowFetchAllTransactionsOverlay = true
            setFetchAllTransactionsView()
        }

        rootView.btnRetrieveTransactions.setOnClickListener { fetchTransactions() }

        return rootView
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        mnitmBalance = menu.findItem(R.id.mnitmBalance)

        mnitmSearchTransactions = menu.findItem(R.id.mnitmSearchTransactions)
        mnitmUpdateTransactions = menu.findItem(R.id.mnitmUpdateTransactions)

        initSearchView()

        initLogicAfterUiInitialized()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnitmUpdateTransactions -> {
                updateAccountsTransactions()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initSearchView() {
        context?.asActivity()?.let { context ->
            (context.getSystemService(Context.SEARCH_SERVICE) as? SearchManager)?.let { searchManager ->
                (mnitmSearchTransactions.actionView as? SearchView)?.let { searchView ->
                    searchView.setSearchableInfo(searchManager.getSearchableInfo(context.componentName))

                    // if imeOptions aren't set like this searchView would take whole remaining screen when focused in landscape mode (see https://stackoverflow.com/questions/15296129/searchview-and-keyboard)
                    val searchInput =
                        searchView.findViewById(androidx.appcompat.R.id.search_src_text) as? EditText
                    searchInput?.imeOptions = EditorInfo.IME_ACTION_SEARCH or EditorInfo.IME_FLAG_NO_EXTRACT_UI

                    searchView.setOnQueryTextListener(searchAccountTransactionsTextListener)
                }
            }
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mnitmNewTransferToSameRemittee -> {
                newTransferToSameRemittee()
                return true
            }
            R.id.mnitmNewTransferWithSameData -> {
                newTransferWithSameData()
                return true
            }
        }

        return super.onContextItemSelected(item)
    }


    private fun initLogicAfterUiInitialized() {
        presenter.addAccountsChangedListener { updateMenuItemsStateAndTransactionsToDisplay() } // on account addition or deletion may menu items' state changes
        presenter.addSelectedBankAccountsChangedListener { updateMenuItemsStateAndTransactionsToDisplay() }

        presenter.addRetrievedAccountTransactionsResponseListener { response ->
            handleGetTransactionsResponseOffUiThread(response)
        }

        updateMenuItemsStateAndTransactionsToDisplay()
    }


    private fun updateMenuItemsStateAndTransactionsToDisplay() {
        context?.asActivity()?.runOnUiThread {
            mnitmSearchTransactions.isVisible = presenter.doSelectedBankAccountsSupportRetrievingAccountTransactions
            mnitmUpdateTransactions.isVisible = presenter.doSelectedBankAccountsSupportRetrievingAccountTransactions

            updateTransactionsToDisplayOnUiThread()
        }
    }

    private fun updateAccountsTransactions() {
        presenter.updateSelectedBankAccountTransactionsAsync { }
    }

    private fun handleGetTransactionsResponseOffUiThread(response: GetTransactionsResponse) {
        context?.asActivity()?.let { activity ->
            activity.runOnUiThread {
                handleGetTransactionsResponseOnUiThread(activity, response)
            }
        }
    }

    private fun handleGetTransactionsResponseOnUiThread(context: Context, response: GetTransactionsResponse) {
        response.retrievedData.forEach { retrievedData ->
            if (retrievedData.successfullyRetrievedData) {
                updateTransactionsToDisplayOnUiThread()
            }
            else if (response.userCancelledAction == false) { // if user cancelled entering TAN then don't show a error message
                AlertDialog.Builder(context)
                    .setMessage(context.getString(R.string.fragment_home_could_not_retrieve_account_transactions,
                        retrievedData.account.displayName, response.errorToShowToUser))
                    .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }


    private fun newTransferToSameRemittee() {
        transactionAdapter.selectedTransaction?.let { selectedTransaction ->
            presenter.showTransferMoneyDialog(TransferMoneyData.fromAccountTransactionWithoutAmountAndUsage(selectedTransaction))
        }
    }

    private fun newTransferWithSameData() {
        transactionAdapter.selectedTransaction?.let { selectedTransaction ->
            presenter.showTransferMoneyDialog(TransferMoneyData.fromAccountTransaction(selectedTransaction))
        }
    }


    private val searchAccountTransactionsTextListener: SearchView.OnQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(query: String): Boolean {
            appliedTransactionsFilter = query

            updateTransactionsToDisplayOnUiThread()

            return true
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            return true
        }
    }


    private fun updateTransactionsToDisplayOnUiThread() {
        transactionAdapter.items = presenter.searchSelectedAccountTransactions(appliedTransactionsFilter)

        mnitmBalance.title = presenter.formatAmount(presenter.balanceOfSelectedBankAccounts)
        mnitmBalance.isVisible = presenter.doSelectedBankAccountsSupportRetrievingBalance

        lytTransactionsSummary.visibility = if (presenter.doSelectedBankAccountsSupportRetrievingBalance) View.VISIBLE else View.GONE

        txtCountTransactions.text = context?.getString(R.string.fragment_home_count_transactions, transactionAdapter.items.size)

        val sumOfDisplayedTransactions = if (appliedTransactionsFilter.isBlank()) presenter.balanceOfSelectedBankAccounts
                                            else transactionAdapter.items.map { it.amount }.sum()
        txtTransactionsBalance.showAmount(presenter, sumOfDisplayedTransactions)

        setRecyclerViewAndNoTransactionsFetchedView()

        setFetchAllTransactionsView()
    }

    private fun setRecyclerViewAndNoTransactionsFetchedView() {
        val transactionsRetrievalState = presenter.selectedBankAccountsTransactionRetrievalState
        val haveTransactionsBeenRetrieved = transactionsRetrievalState == TransactionsRetrievalState.RetrievedTransactions
        val noAccountsAddedYet = presenter.customers.isEmpty()

        rcyvwAccountTransactions.visibility = if (haveTransactionsBeenRetrieved) View.VISIBLE else View.GONE
        lytNoTransactionsFetched.visibility = if (haveTransactionsBeenRetrieved || noAccountsAddedYet) View.GONE else View.VISIBLE
        btnRetrieveTransactions.visibility = if (transactionsRetrievalState == TransactionsRetrievalState.AccountDoesNotSupportFetchingTransactions) View.GONE else View.VISIBLE

        val transactionsRetrievalStateMessageId = when (transactionsRetrievalState) {
            TransactionsRetrievalState.AccountDoesNotSupportFetchingTransactions -> R.string.fragment_home_transactions_retrieval_state_account_does_not_support_retrieving_transactions
            TransactionsRetrievalState.NoTransactionsInRetrievedPeriod -> R.string.fragment_home_transactions_retrieval_state_no_transactions_in_retrieved_period
            TransactionsRetrievalState.NeverRetrievedTransactions -> R.string.fragment_home_transactions_retrieval_state_never_retrieved_transactions
            else -> null
        }
        txtNoTransactionsFetchedMessage.text = transactionsRetrievalStateMessageId?.let { requireContext().getString(transactionsRetrievalStateMessageId) } ?: ""
    }

    private fun setFetchAllTransactionsView() {
        accountsForWhichNotAllTransactionsHaveBeenFetched = presenter.selectedBankAccountsForWhichNotAllTransactionsHaveBeenFetched

        var floatingActionMenuBottomMarginResourceId = R.dimen.fab_margin_bottom_without_toolbar

        if (doNotShowFetchAllTransactionsOverlay || accountsForWhichNotAllTransactionsHaveBeenFetched.isEmpty()
            || presenter.selectedBankAccountsTransactionRetrievalState != TransactionsRetrievalState.RetrievedTransactions) {
            lytFetchAllTransactionsOverlay.visibility = View.GONE
        }
        else {
            lytFetchAllTransactionsOverlay.visibility = View.VISIBLE
            floatingActionMenuBottomMarginResourceId = R.dimen.fab_margin_bottom_with_fetch_all_transactions_overlay
        }

        (requireActivity().findViewById<View>(R.id.floatingActionMenu).layoutParams as? ViewGroup.MarginLayoutParams)?.let { params ->
            params.bottomMargin = requireContext().getDimension(floatingActionMenuBottomMarginResourceId)
        }
    }


    private fun fetchTransactions() {
        presenter.selectedBankAccounts.forEach { account ->
            if (account.haveAllTransactionsBeenFetched) {
                presenter.updateBankAccountTransactionsAsync(account)
            }
            else {
                presenter.fetchAllAccountTransactionsAsync(account)
            }
        }
    }

    private fun fetchAllTransactions() {
        accountsForWhichNotAllTransactionsHaveBeenFetched.forEach { account ->
            presenter.fetchAllAccountTransactionsAsync(account)
        }
    }

}