package com.why.githubtrendyrepos.viewmodels

data class RepoViewModel(
    val name: String,
    val description: String,
    val author: String,
    val starsCount: Int,
    val repoImageLink: String,
) {

    private lateinit var _starsCountCache: String

    private fun formatAsOneDigitDecimal(f: Float): String {
        val s = f.toString()
        val i = s.indexOf(".")
        return "${s.substring(0, i + 2)}k"
    }

    private fun formatStarsCount() = when {
        starsCount < 1000 -> starsCount.toString()
        starsCount % 1000 == 0 -> "${starsCount / 1000}k"
        else -> {
            val f = starsCount / 1000f
            when {
                starsCount < 10000 -> formatAsOneDigitDecimal(f)
                else -> {
                    val wholeNumber = f.toInt()
                    val decimalPart = starsCount - (wholeNumber * 1000)

                    val threshold = when {
                        starsCount < 100000 -> 100
                        else -> 1000
                    }

                    if (decimalPart < threshold)
                        "${wholeNumber}k"
                    else formatAsOneDigitDecimal(f)
                }
            }
        }
    }

    val starsCountStr: String
        get() {
            if (!::_starsCountCache.isInitialized)
                _starsCountCache = formatStarsCount()

            return _starsCountCache
        }
}
