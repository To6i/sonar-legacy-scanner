
public class OuterClassWithInnerLegacyClass
{
    private OuterClassWithInnerLegacyClass()
    {
    }

    @Override
    public String toString()
    {
        return "something";
    }

    @Legacy
    public static class InnerLegacyClass implements Runnable
    {
        public void run()
        {
            // NOP
        }
    }
}