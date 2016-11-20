
@Legacy
public enum LegacyEnum
{
    VALUE1(1), VALUE2(2), VALUE3(3);

    private int field;

    private LegacyEnum(int param)
    {
        field = param;
    }

    public int get()
    {
        return field;
    }
}
