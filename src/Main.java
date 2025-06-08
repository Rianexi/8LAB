public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("\u001B[31mУкажите режим: 'client' или 'server'\u001B[0m");
            System.exit(1);
        }
        try {
            switch (args[0].toLowerCase()) {
                case "client":
                    client.Client.main(new String[]{});
                    break;
                case "server":
                    server.Server.main(new String[]{});
                    break;
                default:
                    System.err.println("\u001B[31mНеверный режим: выберите 'client' или 'server'\u001B[0m");
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("\u001B[31mОшибка запуска: " + e.getMessage() + "\u001B[0m");
            System.exit(1);
        }
    }
}