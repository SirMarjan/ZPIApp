package com.zpi.zpiapp.careAssistants

import com.zpi.zpiapp.model.CareAssistant


class CareAssistantsPresenterMock(private val careAssistantsView: CareAssistantsContract.View) : CareAssistantsContract.Presenter {
    private val careAssistantDAO: CareAssistantsMockDAO = CareAssistantsMockDAO()

    init {
        careAssistantsView.setPresenter(this)
    }

    override fun start() {
        refreshCareAssistants()
    }

    override fun refreshCareAssistants() {
        val list = careAssistantDAO.userList
        if (list == null) {
            careAssistantsView.showConnectionError()
        } else if (!list.isEmpty()){
            careAssistantsView.showCareAssistants()
            list.forEach{careAssistantsView.addCareAssistant(it)}
        }
        else
            careAssistantsView.showCareAssistantsNotFound()
    }

    override fun addNewCareAssistant(login: String) {
        val added = careAssistantDAO.allList.firstOrNull{careAssistant -> careAssistant.name == login }
        if(added != null){
            if(careAssistantDAO.userList.contains(added))
                careAssistantsView.showSnackBarError("Użytkownik jest już dodany")
            else{
                val empty = careAssistantDAO.userList.isEmpty()
                careAssistantDAO.userList.add(added)
                careAssistantsView.addCareAssistant(added)
                careAssistantsView.clearAddCareAssistant()

                if(empty) careAssistantsView.showCareAssistants()
            }

        } else careAssistantsView.showSnackBarError("nie znaleziono podaengo urzytkownika")

    }

    override fun checkRemovingCareAssistants(id: Int) {
        val remove = careAssistantDAO.userList.first { careAssistant -> careAssistant.idCareAssistant==id }
        careAssistantsView.showRemoveDialog( remove )
    }

    override fun removeCareAssistant(id: Int) {
        val remove = careAssistantDAO.userList.first { careAssistant -> careAssistant.idCareAssistant==id }
        careAssistantDAO.userList.remove(remove)
        careAssistantsView.removeCareAssistant(remove)
        if (careAssistantDAO.userList.isEmpty())
            careAssistantsView.showCareAssistantsNotFound()
    }

    private class CareAssistantsMockDAO{
        val allList = mutableListOf(
                CareAssistant(1,"Tomek","Radca", "sdasd"),
                CareAssistant(2,"Milek","Mosona", "sdasdas"),
                CareAssistant(3,"Olek","Katorga", "sadas"))

        val userList = mutableListOf(allList[1],allList[0])
    }
}