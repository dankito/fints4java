package net.dankito.banking.fints4java.android.ui.adapter

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import kotlinx.android.synthetic.main.list_item_bank_info.view.*
import net.dankito.banking.fints4java.android.R
import net.dankito.banking.fints4java.android.ui.adapter.filter.BankInfoFilter
import net.dankito.fints.banks.BankFinder
import net.dankito.fints.model.BankInfo
import net.dankito.utils.android.ui.adapter.ListAdapter


open class BankListAdapter(protected val bankFinder: BankFinder = BankFinder()) : ListAdapter<BankInfo>(), Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val item = getItem(position)

        val inflater = parent?.context?.getSystemService(LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        val view = convertView ?: inflater?.inflate(R.layout.list_item_bank_info, parent, false)

        view?.let {
            // view.imgSupportsFints30.setImageResource(if (item.supportsFinTs3_0) ) // TODO

            view.txtvwBankName.text = item.name

            view.txtvwBankCode.text = item.bankCode

            view.txtvwBankAddress.text = item.postalCode + " " + item.city
        }

        return view
    }


    override fun getFilter(): Filter {
        return BankInfoFilter(bankFinder) {
            this.setItems(it)
        }
    }

}