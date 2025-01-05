class ReentrantTest
{
    public static synchronized void a()
    {
        System.out.println("Starting method: a()") ;
        b() ;
        System.out.println("Finishing method: a()") ;
    }
    public static synchronized void b()
    {
        System.out.println("Executing method: b()");
    }

    public static void main(String[] args) {
        a();
    }
}