package share.commands;

public interface CommandType {
    // Основные команды
    String EXIT = "exit";  // выполняется на стороне клиента
    String HELP = "help";  // выполняется на стороне клиента
    String INFO = "info";

    // Команды для работы с коллекцией
    String SHOW = "show";
    String ADD = "add";
    String UPDATE = "update";
    String REMOVE_BY_ID = "remove_by_id";
    String CLEAR = "clear";

    // Условные команды
    String ADD_IF_MAX = "add_if_max";
    String REMOVE_GREATER = "remove_greater";

    // Информационные команды
    String HISTORY = "history";
    String GROUP_COUNTING_BY_COLOR = "group_counting_by_color";
    String COUNT_GREATER_THAN_AGE = "count_greater_than_age";
    String FILTER_LESS_THAN_CHARACTER = "filter_less_than_character";

    // Системные команды
    String EXECUTE_SCRIPT = "execute_script"; // выполняется на стороне клиента
    String GET = "get"; // выполняется только на стороне сервера (нет в списке команд на клиентской стороне)

    // Регистрация/авторизация
    String REGISTER = "register";
    String LOGIN = "login";
}