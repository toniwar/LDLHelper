package com.tonivar.ldlhelper.presentation.fragments

interface FragmentListener {
    fun setAction(fragmentName: FragmentName, args: List<String>?)

    companion object{
        enum class FragmentName{
            START,
            MENU,
            TEST,
            SEARCH
        }
    }
}
