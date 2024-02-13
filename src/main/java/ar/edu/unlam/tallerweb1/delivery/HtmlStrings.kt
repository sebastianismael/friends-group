package ar.edu.unlam.tallerweb1.delivery

object HtmlStrings {
        const val NO_EXPENSES = """
            Sin gastos =) 
            <h4>
                <a href=\"goto-add-friend?user=%s\"\t>Agregar Amigo</a></br>
                <a href=\"goto-add-expent?user=%s\"\t>Agregar Gasto</a></br>
            </h4>"""
        const val EXPENSES_HEADER = """<h4>
            Gastos:      
                <a href=\"goto-add-friend?user=%s\"\t>Agregar Amigo</a></br>
                <a href=\"goto-add-expent?user=%s\"\t>Agregar Gasto</a></br>
                <a href=\"balance?user=%s\"\t>Ver Balance</a></br>
            </h4>"""
        const val BALANCE_HEADER = """<div id=\"balance\" style=\"width:1000; height:1000;\"><h4>Balance:</h4>"""
        const val FRIEND_ADDED = "Se ha agregado un nuevo amigo a tu grupo"
        const val ADD_EXPENT_NO_DETAIL = "El detalle es invalido"
        const val ADD_EXPENT_SUCCESS = "Gasto agregado!"
        const val ADD_EXPENT_NO_AMOUNT = "El monto es invalido"
        const val EXPEND_SINCE_DAYS = "Hace %s día(s)"
        const val EXPEND_SINCE_MINUTES = "Hace %s min(s)"
        const val TAB = "&emsp;"
        const val EURO = "&euro;"
        const val NEW_LINE = "<br>"
        const val CLOSE_DIV = "</div>"
        const val GENERIC_ERROR = "Ha ocurrido un error inesperado. Reintente mas tarde =("
}
