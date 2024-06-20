package ar.edu.unlam.tallerweb1.delivery

import ar.edu.unlam.tallerweb1.delivery.api.ApiListSharedExpenses
import ar.edu.unlam.tallerweb1.delivery.api.ApiShowBalance
import ar.edu.unlam.tallerweb1.delivery.controllers.*
import ar.edu.unlam.tallerweb1.delivery.util.CleanData
import ar.edu.unlam.tallerweb1.delivery.util.CreateSchema
import ar.edu.unlam.tallerweb1.delivery.util.LoadData
import ar.edu.unlam.tallerweb1.domain.Repositories.getSharedExpensesRepository
import ar.edu.unlam.tallerweb1.domain.UseCases
import ar.edu.unlam.tallerweb1.domain.UseCases.addExpentToGroup
import ar.edu.unlam.tallerweb1.domain.UseCases.addFriendsToGroup
import ar.edu.unlam.tallerweb1.domain.UseCases.getBalance

class Controllers {
    companion object {
        private val controllers: MutableMap<String, Controller> = HashMap()

        // TODO agregar manejo de HTTP methods
        init {
            controllers["/"] = Index()
            controllers["/list-expenses"] = ListSharedExpenses(getSharedExpensesRepository())
            controllers["/goto-add-friend"] = GoToAddFriend()
            controllers["/add-friend"] = AddFriend(addFriendsToGroup())
            controllers["/balance"] = ShowBalance(getBalance())
            controllers["/goto-add-expent"] = GoToAddExpent()
            controllers["/add-expent"] = AddExpent(addExpentToGroup())

            controllers["/alive"] = Alive()
            controllers["/load-data"] = LoadData()
            controllers["/clean-data"] = CleanData()
            controllers["/create-schema"] = CreateSchema()

            controllers["/api/list-expenses"] = ApiListSharedExpenses(getSharedExpensesRepository())
            controllers["/api/balance"] = ApiShowBalance(getBalance())
        }

        fun resolve(path: String)= getControllerFor(path).takeUnless { it == null } ?: throw Exception()

        private fun getControllerFor(path: String) =
            if (path.startsWith("/sitio-1.0")) // TODO esto es un hack, buscar una forma de hacerlo bien
                controllers[path.replace("/sitio-1.0", "")]
            else controllers[path]

    }

}
