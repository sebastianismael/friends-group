package ar.edu.unlam.tallerweb1.delivery;

public class HtmlStrings {
    public static final String NO_EXPENSES = "Sin gastos =) " +
            "<h4>" +
            "<a href=\"goto-add-friend?user=%s\"\t>Agregar Amigo</a></br>" +
            "<a href=\"goto-add-expent?user=%s\"\t>Agregar Gasto</a></br>" +
            "</h4>";
    public static final String EXPENSES_HEADER =
            "<h4>" +
            "Gastos:      " +
            "<a href=\"goto-add-friend?user=%s\"\t>Agregar Amigo</a></br>" +
            "<a href=\"goto-add-expent?user=%s\"\t>Agregar Gasto</a></br>" +
            "<a href=\"balance?user=%s\"\t>Ver Balance</a></br>" +
            "</h4>";
    public static final String BALANCE_HEADER =
            "<div id=\"balance\" style=\"width:1000; height:1000;\">" +
            "<h4>Balance:</h4>";
    public static final String FRIEND_ADDED = "Se ha agregado un nuevo amigo a tu grupo";
    public static final String ADD_EXPENT_NO_DETAIL = "El detalle es invalido";
    public static final String ADD_EXPENT_SUCCESS = "Gasto agregado!";
    public static final String ADD_EXPENT_NO_AMOUNT = "El monto es invalido";
    public static final String EXPEND_SINCE_DAYS = "Hace %s día(s)";
    public static final String EXPEND_SINCE_MINUTES = "Hace %s min(s)";
    public static final String TAB = "&emsp;";
    public static final String EURO = "&euro;";
    public static final String NEW_LINE = "<br>";
    public static final String CLOSE_DIV = "</div>";
    public static final String GENERIC_ERROR = "Ha ocurrido un error inesperado. Reintente mas tarde =(";
}
