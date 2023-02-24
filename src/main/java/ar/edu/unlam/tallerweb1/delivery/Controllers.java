package ar.edu.unlam.tallerweb1.delivery;

import ar.edu.unlam.tallerweb1.delivery.api.ApiListSharedExpenses;
import ar.edu.unlam.tallerweb1.delivery.api.ApiShowBalance;
import ar.edu.unlam.tallerweb1.delivery.controllers.*;
import ar.edu.unlam.tallerweb1.delivery.util.CleanData;
import ar.edu.unlam.tallerweb1.delivery.util.LoadData;

import java.util.HashMap;
import java.util.Map;

import static ar.edu.unlam.tallerweb1.domain.Repositories.getSharedExpensesRepository;
import static ar.edu.unlam.tallerweb1.domain.Services.getFriendsGroupService;

public class Controllers {
    private static final Map<String, Controller> controllers = new HashMap<>();

    // TODO agregar manejo de HTTP methods
    static {
        controllers.put("/list-expenses", new ListSharedExpenses(getSharedExpensesRepository()));
        controllers.put("/goto-add-friend", new GoToAddFriend());
        controllers.put("/add-friend", new AddFriend(getFriendsGroupService()));
        controllers.put("/balance", new ShowBalance(getFriendsGroupService()));
        controllers.put("/goto-add-expent", new GoToAddExpent());
        controllers.put("/add-expent", new AddExpent(getFriendsGroupService()));

        controllers.put("/alive", new Alive());
        controllers.put("/load-data", new LoadData());
        controllers.put("/clean-data", new CleanData());

        controllers.put("/api/list-expenses", new ApiListSharedExpenses(getSharedExpensesRepository()));
        controllers.put("/api/balance", new ApiShowBalance(getFriendsGroupService()));
    }

    public static Controller resolve(String path){
        if(path.startsWith("/sitio-1.0")){ // TODO esto es un hack, buscar una forma de hacerlo bien
            return controllers.get(path.replace("/sitio-1.0", ""));
        }
        return controllers.get(path);
    }
}
