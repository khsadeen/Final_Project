public class FeatureManager {
    private static Cards loggedInCard;

    public static void setLoggedInCard(Cards card) {
        loggedInCard = card;
    }

    public static Cards getLoggedInCard() {
        return loggedInCard;
    }
}
