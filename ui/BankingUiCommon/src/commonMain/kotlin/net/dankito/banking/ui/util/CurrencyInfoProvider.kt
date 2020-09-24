package net.dankito.banking.ui.util


open class CurrencyInfoProvider : ICurrencyInfoProvider {

    companion object {

        // extracted from JVM 8 // TODO: may use the list from Joda Money
        val infos = listOf(
            CurrencyInfo("Luxembourgian Franc", "LUF", 442, "LUF", 0),
            CurrencyInfo("Serbian Dinar (2002–2006)", "CSD", 891, "CSD", 2),
            CurrencyInfo("French Franc", "FRF", 250, "FRF", 2),
            CurrencyInfo("Albanian Lek", "ALL", 8, "ALL", 2),
            CurrencyInfo("Iraqi Dinar", "IQD", 368, "IQD", 3),
            CurrencyInfo("Bulgarian Hard Lev", "BGL", 100, "BGL", 2),
            CurrencyInfo("Sierra Leonean Leone", "SLL", 694, "SLL", 2),
            CurrencyInfo("Guinea-Bissau Peso", "GWP", 624, "GWP", 2),
            CurrencyInfo("Turkish Lira", "TRY", 949, "TRY", 2),
            CurrencyInfo("Belgian Franc", "BEF", 56, "BEF", 0),
            CurrencyInfo("Haitian Gourde", "HTG", 332, "HTG", 2),
            CurrencyInfo("Thai Baht", "THB", 764, "THB", 2),
            CurrencyInfo("Bahraini Dinar", "BHD", 48, "BHD", 3),
            CurrencyInfo("Sudanese Pound", "SDG", 938, "SDG", 2),
            CurrencyInfo("Chinese Yuan", "CNY", 156, "CN¥", 2),
            CurrencyInfo("Mozambican Metical (1980–2006)", "MZM", 508, "MZM", 2),
            CurrencyInfo("Maldivian Rufiyaa", "MVR", 462, "MVR", 2),
            CurrencyInfo("Austrian Schilling", "ATS", 40, "ATS", 2),
            CurrencyInfo("Bosnia-Herzegovina Convertible Mark", "BAM", 977, "BAM", 2),
            CurrencyInfo("Afghan Afghani", "AFN", 971, "AFN", 2),
            CurrencyInfo("Sucre", "XSU", 994, "XSU", -1),
            CurrencyInfo("East Caribbean Dollar", "XCD", 951, "EC$", 2),
            CurrencyInfo("Finnish Markka", "FIM", 246, "FIM", 2),
            CurrencyInfo("Platinum", "XPT", 962, "XPT", -1),
            CurrencyInfo("Cuban Convertible Peso", "CUC", 931, "CUC", 2),
            CurrencyInfo("Zambian Kwacha", "ZMW", 967, "ZMW", 2),
            CurrencyInfo("Norwegian Krone", "NOK", 578, "NOK", 2),
            CurrencyInfo("Mozambican Metical", "MZN", 943, "MZN", 2),
            CurrencyInfo("Swedish Krona", "SEK", 752, "SEK", 2),
            CurrencyInfo("Paraguayan Guarani", "PYG", 600, "PYG", 0),
            CurrencyInfo("United Arab Emirates Dirham", "AED", 784, "AED", 2),
            CurrencyInfo("Nepalese Rupee", "NPR", 524, "NPR", 2),
            CurrencyInfo("Nigerian Naira", "NGN", 566, "NGN", 2),
            CurrencyInfo("Congolese Franc", "CDF", 976, "CDF", 2),
            CurrencyInfo("Laotian Kip", "LAK", 418, "LAK", 2),
            CurrencyInfo("Cape Verdean Escudo", "CVE", 132, "CVE", 2),
            CurrencyInfo("French Gold Franc", "XFO", 0, "XFO", -1),
            CurrencyInfo("Timorese Escudo", "TPE", 626, "TPE", 0),
            CurrencyInfo("Zimbabwean Dollar (2008)", "ZWR", 935, "ZWR", 2),
            CurrencyInfo("Palladium", "XPD", 964, "XPD", -1),
            CurrencyInfo("Mexican Investment Unit", "MXV", 979, "MXV", 2),
            CurrencyInfo("West African CFA Franc", "XOF", 952, "CFA", 0),
            CurrencyInfo("Testing Currency Code", "XTS", 963, "XTS", -1),
            CurrencyInfo("Maltese Lira", "MTL", 470, "MTL", 2),
            CurrencyInfo("Mauritian Rupee", "MUR", 480, "MUR", 2),
            CurrencyInfo("Yugoslavian New Dinar (1994–2002)", "YUM", 891, "YUM", 2),
            CurrencyInfo("Djiboutian Franc", "DJF", 262, "DJF", 0),
            CurrencyInfo("Vietnamese Dong", "VND", 704, "₫", 0),
            CurrencyInfo("Russian Rouble (1991–1998)", "RUR", 810, "RUR", 2),
            CurrencyInfo("Indian Rupee", "INR", 356, "₹", 2),
            CurrencyInfo("Turkmenistani Manat (1993–2009)", "TMM", 795, "TMM", 2),
            CurrencyInfo("Philippine Piso", "PHP", 608, "PHP", 2),
            CurrencyInfo("Panamanian Balboa", "PAB", 590, "PAB", 2),
            CurrencyInfo("Lithuanian Litas", "LTL", 440, "LTL", 2),
            CurrencyInfo("Chilean Peso", "CLP", 152, "CLP", 0),
            CurrencyInfo("Ugandan Shilling", "UGX", 800, "UGX", 0),
            CurrencyInfo("Fijian Dollar", "FJD", 242, "FJD", 2),
            CurrencyInfo("Iranian Rial", "IRR", 364, "IRR", 2),
            CurrencyInfo("Bhutanese Ngultrum", "BTN", 64, "BTN", 2),
            CurrencyInfo("Jamaican Dollar", "JMD", 388, "JMD", 2),
            CurrencyInfo("Venezuelan Bolívar (1871–2008)", "VEB", 862, "VEB", 2),
            CurrencyInfo("Romanian Leu (1952–2006)", "ROL", 946, "ROL", 2),
            CurrencyInfo("Belize Dollar", "BZD", 84, "BZD", 2),
            CurrencyInfo("Malawian Kwacha", "MWK", 454, "MWK", 2),
            CurrencyInfo("Eritrean Nakfa", "ERN", 232, "ERN", 2),
            CurrencyInfo("Salvadoran Colón", "SVC", 222, "SVC", 2),
            CurrencyInfo("British Pound", "GBP", 826, "£", 2),
            CurrencyInfo("Swiss Franc", "CHF", 756, "CHF", 2),
            CurrencyInfo("Guinean Franc", "GNF", 324, "GNF", 0),
            CurrencyInfo("Kazakhstani Tenge", "KZT", 398, "KZT", 2),
            CurrencyInfo("Greek Drachma", "GRD", 300, "GRD", 0),
            CurrencyInfo("Italian Lira", "ITL", 380, "ITL", 0),
            CurrencyInfo("Portuguese Escudo", "PTE", 620, "PTE", 0),
            CurrencyInfo("Israeli New Shekel", "ILS", 376, "₪", 2),
            CurrencyInfo("South Sudanese Pound", "SSP", 728, "SSP", 2),
            CurrencyInfo("Samoan Tala", "WST", 882, "WST", 2),
            CurrencyInfo("Tunisian Dinar", "TND", 788, "TND", 3),
            CurrencyInfo("South African Rand", "ZAR", 710, "ZAR", 2),
            CurrencyInfo("Guatemalan Quetzal", "GTQ", 320, "GTQ", 2),
            CurrencyInfo("Mongolian Tugrik", "MNT", 496, "MNT", 2),
            CurrencyInfo("Bulgarian Lev", "BGN", 975, "BGN", 2),
            CurrencyInfo("Colombian Peso", "COP", 170, "COP", 2),
            CurrencyInfo("Algerian Dinar", "DZD", 12, "DZD", 2),
            CurrencyInfo("Brazilian Real", "BRL", 986, "R$", 2),
            CurrencyInfo("CFP Franc", "XPF", 953, "CFPF", 0),
            CurrencyInfo("Surinamese Dollar", "SRD", 968, "SRD", 2),
            CurrencyInfo("Swazi Lilangeni", "SZL", 748, "SZL", 2),
            CurrencyInfo("European Monetary Unit", "XBB", 956, "XBB", -1),
            CurrencyInfo("Azerbaijani Manat", "AZN", 944, "AZN", 2),
            CurrencyInfo("Kyrgystani Som", "KGS", 417, "KGS", 2),
            CurrencyInfo("Bolivian Boliviano", "BOB", 68, "BOB", 2),
            CurrencyInfo("Uzbekistani Som", "UZS", 860, "UZS", 2),
            CurrencyInfo("São Tomé & Príncipe Dobra (1977–2017)", "STD", 678, "STD", 2),
            CurrencyInfo("Euro", "EUR", 978, "€", 2),
            CurrencyInfo("Kuwaiti Dinar", "KWD", 414, "KWD", 3),
            CurrencyInfo("St Helena Pound", "SHP", 654, "SHP", 2),
            CurrencyInfo("Andorran Peseta", "ADP", 20, "ADP", 0),
            CurrencyInfo("Belarusian New Rouble (1994–1999)", "BYB", 112, "BYB", 0),
            CurrencyInfo("French UIC-Franc", "XFU", 0, "XFU", -1),
            CurrencyInfo("Mexican Peso", "MXN", 484, "MX$", 2),
            CurrencyInfo("Central African CFA Franc", "XAF", 950, "FCFA", 0),
            CurrencyInfo("Cayman Islands Dollar", "KYD", 136, "KYD", 2),
            CurrencyInfo("Danish Krone", "DKK", 208, "DKK", 2),
            CurrencyInfo("Russian Rouble", "RUB", 643, "RUB", 2),
            CurrencyInfo("Bolivian Mvdol", "BOV", 984, "BOV", 2),
            CurrencyInfo("Nicaraguan Córdoba", "NIO", 558, "NIO", 2),
            CurrencyInfo("US Dollar", "USD", 840, "US$", 2),
            CurrencyInfo("Hong Kong Dollar", "HKD", 344, "HK$", 2),
            CurrencyInfo("Botswanan Pula", "BWP", 72, "BWP", 2),
            CurrencyInfo("Tajikistani Somoni", "TJS", 972, "TJS", 2),
            CurrencyInfo("Peruvian Sol", "PEN", 604, "PEN", 2),
            CurrencyInfo("Colombian Real Value Unit", "COU", 970, "COU", 2),
            CurrencyInfo("Malaysian Ringgit", "MYR", 458, "MYR", 2),
            CurrencyInfo("Dutch Guilder", "NLG", 528, "NLG", 2),
            CurrencyInfo("Serbian Dinar", "RSD", 941, "RSD", 2),
            CurrencyInfo("Armenian Dram", "AMD", 51, "AMD", 2),
            CurrencyInfo("Guyanaese Dollar", "GYD", 328, "GYD", 2),
            CurrencyInfo("Barbadian Dollar", "BBD", 52, "BBD", 2),
            CurrencyInfo("Gambian Dalasi", "GMD", 270, "GMD", 2),
            CurrencyInfo("US Dollar (Next day)", "USN", 997, "USN", 2),
            CurrencyInfo("Zimbabwean Dollar (2009)", "ZWL", 932, "ZWL", 2),
            CurrencyInfo("US Dollar (Same day)", "USS", 998, "USS", 2),
            CurrencyInfo("Turkish Lira (1922–2005)", "TRL", 792, "TRL", 0),
            CurrencyInfo("Tongan Paʻanga", "TOP", 776, "TOP", 2),
            CurrencyInfo("Special Drawing Rights", "XDR", 960, "XDR", -1),
            CurrencyInfo("Angolan Kwanza", "AOA", 973, "AOA", 2),
            CurrencyInfo("Bahamian Dollar", "BSD", 44, "BSD", 2),
            CurrencyInfo("Vanuatu Vatu", "VUV", 548, "VUV", 0),
            CurrencyInfo("Icelandic Króna", "ISK", 352, "ISK", 0),
            CurrencyInfo("German Mark", "DEM", 276, "DEM", 2),
            CurrencyInfo("Gold", "XAU", 959, "XAU", -1),
            CurrencyInfo("Australian Dollar", "AUD", 36, "A$", 2),
            CurrencyInfo("Turkmenistani Manat", "TMT", 934, "TMT", 2),
            CurrencyInfo("Liberian Dollar", "LRD", 430, "LRD", 2),
            CurrencyInfo("Croatian Kuna", "HRK", 191, "HRK", 2),
            CurrencyInfo("Trinidad & Tobago Dollar", "TTD", 780, "TTD", 2),
            CurrencyInfo("WIR Franc", "CHW", 948, "CHW", 2),
            CurrencyInfo("Yemeni Rial", "YER", 886, "YER", 2),
            CurrencyInfo("Somali Shilling", "SOS", 706, "SOS", 2),
            CurrencyInfo("Moldovan Leu", "MDL", 498, "MDL", 2),
            CurrencyInfo("Georgian Lari", "GEL", 981, "GEL", 2),
            CurrencyInfo("Rwandan Franc", "RWF", 646, "RWF", 0),
            CurrencyInfo("Afghan Afghani (1927–2002)", "AFA", 4, "AFA", 2),
            CurrencyInfo("Honduran Lempira", "HNL", 340, "HNL", 2),
            CurrencyInfo("Venezuelan Bolívar Soberano", "VES", 928, "VES", 2),
            CurrencyInfo("Cambodian Riel", "KHR", 116, "KHR", 2),
            CurrencyInfo("South Korean Won", "KRW", 410, "₩", 0),
            CurrencyInfo("Sudanese Dinar (1992–2007)", "SDD", 736, "SDD", 2),
            CurrencyInfo("Venezuelan Bolívar", "VEF", 937, "VEF", 2),
            CurrencyInfo("Costa Rican Colón", "CRC", 188, "CRC", 2),
            CurrencyInfo("Singapore Dollar", "SGD", 702, "SGD", 2),
            CurrencyInfo("Jordanian Dinar", "JOD", 400, "JOD", 3),
            CurrencyInfo("Saudi Riyal", "SAR", 682, "SAR", 2),
            CurrencyInfo("Irish Pound", "IEP", 372, "IEP", 2),
            CurrencyInfo("Zambian Kwacha (1968–2012)", "ZMK", 894, "ZMK", 2),
            CurrencyInfo("Omani Rial", "OMR", 512, "OMR", 3),
            CurrencyInfo("Canadian Dollar", "CAD", 124, "CA$", 2),
            CurrencyInfo("European Composite Unit", "XBA", 955, "XBA", -1),
            CurrencyInfo("Slovenian Tolar", "SIT", 705, "SIT", 2),
            CurrencyInfo("WIR Euro", "CHE", 947, "CHE", 2),
            CurrencyInfo("Egyptian Pound", "EGP", 818, "EGP", 2),
            CurrencyInfo("Argentine Peso", "ARS", 32, "ARS", 2),
            CurrencyInfo("Silver", "XAG", 961, "XAG", -1),
            CurrencyInfo("ZWN", "ZWN", 942, "ZWN", 2),
            CurrencyInfo("São Tomé & Príncipe Dobra", "STN", 930, "STN", 2),
            CurrencyInfo("Mauritanian Ouguiya", "MRU", 929, "MRU", 2),
            CurrencyInfo("Hungarian Forint", "HUF", 348, "HUF", 2),
            CurrencyInfo("Chilean Unit of Account (UF)", "CLF", 990, "CLF", 4),
            CurrencyInfo("Mauritanian Ouguiya (1973–2017)", "MRO", 478, "MRO", 2),
            CurrencyInfo("Ghanaian Cedi (1979–2007)", "GHC", 288, "GHC", 2),
            CurrencyInfo("New Zealand Dollar", "NZD", 554, "NZ$", 2),
            CurrencyInfo("Sri Lankan Rupee", "LKR", 144, "LKR", 2),
            CurrencyInfo("Papua New Guinean Kina", "PGK", 598, "PGK", 2),
            CurrencyInfo("Cypriot Pound", "CYP", 196, "CYP", 2),
            CurrencyInfo("Macanese Pataca", "MOP", 446, "MOP", 2),
            CurrencyInfo("Bermudan Dollar", "BMD", 60, "BMD", 2),
            CurrencyInfo("Cuban Peso", "CUP", 192, "CUP", 2),
            CurrencyInfo("Solomon Islands Dollar", "SBD", 90, "SBD", 2),
            CurrencyInfo("Unknown Currency", "XXX", 999, "XXX", -1),
            CurrencyInfo("Ethiopian Birr", "ETB", 230, "ETB", 2),
            CurrencyInfo("Libyan Dinar", "LYD", 434, "LYD", 3),
            CurrencyInfo("Syrian Pound", "SYP", 760, "SYP", 2),
            CurrencyInfo("Latvian Lats", "LVL", 428, "LVL", 2),
            CurrencyInfo("Bangladeshi Taka", "BDT", 50, "BDT", 2),
            CurrencyInfo("Moroccan Dirham", "MAD", 504, "MAD", 2),
            CurrencyInfo("Lesotho Loti", "LSL", 426, "LSL", 2),
            CurrencyInfo("Malagasy Ariary", "MGA", 969, "MGA", 2),
            CurrencyInfo("Qatari Rial", "QAR", 634, "QAR", 2),
            CurrencyInfo("Ukrainian Hryvnia", "UAH", 980, "UAH", 2),
            CurrencyInfo("Comorian Franc", "KMF", 174, "KMF", 0),
            CurrencyInfo("Aruban Florin", "AWG", 533, "AWG", 2),
            CurrencyInfo("Namibian Dollar", "NAD", 516, "NAD", 2),
            CurrencyInfo("Myanmar Kyat", "MMK", 104, "MMK", 2),
            CurrencyInfo("Spanish Peseta", "ESP", 724, "ESP", 0),
            CurrencyInfo("Japanese Yen", "JPY", 392, "JP¥", 0),
            CurrencyInfo("Indonesian Rupiah", "IDR", 360, "IDR", 2),
            CurrencyInfo("North Korean Won", "KPW", 408, "KPW", 2),
            CurrencyInfo("European Unit of Account (XBD)", "XBD", 958, "XBD", -1),
            CurrencyInfo("Brunei Dollar", "BND", 96, "BND", 2),
            CurrencyInfo("Falkland Islands Pound", "FKP", 238, "FKP", 2),
            CurrencyInfo("Uruguayan Peso", "UYU", 858, "UYU", 2),
            CurrencyInfo("Azerbaijani Manat (1993–2006)", "AZM", 31, "AZM", 2),
            CurrencyInfo("Burundian Franc", "BIF", 108, "BIF", 0),
            CurrencyInfo("Dominican Peso", "DOP", 214, "DOP", 2),
            CurrencyInfo("Belarusian Rouble", "BYN", 933, "BYN", 2),
            CurrencyInfo("Malagasy Franc", "MGF", 450, "MGF", 0),
            CurrencyInfo("Slovak Koruna", "SKK", 703, "SKK", 2),
            CurrencyInfo("Gibraltar Pound", "GIP", 292, "GIP", 2),
            CurrencyInfo("New Taiwan Dollar", "TWD", 901, "NT$", 2),
            CurrencyInfo("Romanian Leu", "RON", 946, "RON", 2),
            CurrencyInfo("European Unit of Account (XBC)", "XBC", 957, "XBC", -1),
            CurrencyInfo("Netherlands Antillean Guilder", "ANG", 532, "ANG", 2),
            CurrencyInfo("Polish Zloty", "PLN", 985, "PLN", 2),
            CurrencyInfo("Ghanaian Cedi", "GHS", 936, "GHS", 2),
            CurrencyInfo("Lebanese Pound", "LBP", 422, "LBP", 2),
            CurrencyInfo("AYM", "AYM", 945, "AYM", 2),
            CurrencyInfo("Uruguayan Peso (Indexed Units)", "UYI", 940, "UYI", 0),
            CurrencyInfo("Czech Koruna", "CZK", 203, "CZK", 2),
            CurrencyInfo("Tanzanian Shilling", "TZS", 834, "TZS", 2),
            CurrencyInfo("Seychellois Rupee", "SCR", 690, "SCR", 2),
            CurrencyInfo("Zimbabwean Dollar (1980–2008)", "ZWD", 716, "ZWD", 2),
            CurrencyInfo("Estonian Kroon", "EEK", 233, "EEK", 2),
            CurrencyInfo("Pakistani Rupee", "PKR", 586, "PKR", 2),
            CurrencyInfo("Surinamese Guilder", "SRG", 740, "SRG", 2),
            CurrencyInfo("ADB Unit of Account", "XUA", 965, "XUA", -1),
            CurrencyInfo("Macedonian Denar", "MKD", 807, "MKD", 2),
            CurrencyInfo("Kenyan Shilling", "KES", 404, "KES", 2),
            CurrencyInfo("Belarusian Rouble (2000–2016)", "BYR", 974, "BYR", 0)
        )

        val symbolsForIsoCode = infos.map { it.isoCode to it.symbol }.toMap()

        val Euro = infos.first { it.isoCode == "EUR" }

    }


    override val userDefaultCurrencyInfo: CurrencyInfo
        get() = Euro // TODO: implement real lookup by current Locale

    override val currencyInfos: List<CurrencyInfo>
        get() = ArrayList(infos)


    override fun getInfoForIsoCode(isoCode: String): CurrencyInfo? {
        return infos.firstOrNull { it.isoCode == isoCode }
    }

    override fun getCurrencySymbolForIsoCode(isoCode: String): String? {
        return symbolsForIsoCode[isoCode]
    }

    override fun getCurrencySymbolForIsoCodeOr(isoCode: String, defaultValue: String): String {
        return getCurrencySymbolForIsoCode(isoCode) ?: defaultValue
    }

    override fun getCurrencySymbolForIsoCodeOrEuro(isoCode: String): String {
        return getCurrencySymbolForIsoCodeOr(isoCode, "€")
    }

}