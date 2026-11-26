public class StepStats
{
    private final int thousands;
    private int hundreds;
    private int tens;

    public StepStats(int thousands, int hundreds, int tens)
    {
        this.thousands = thousands;
        this.hundreds = hundreds;
        this.tens = tens;
    }

    public void prettyPrint()
    {
        String output = "";

        int parts = 0;

        if (thousands > 0)
        {
            parts++;
        }

        if (hundreds > 0)
        {
            parts++;
        }

        if (tens > 0)
        {
            parts++;
        }

        int currentPart = 0;

        if (thousands > 0)
        {
            currentPart++;

            output += thousands + " ";

            if (thousands == 1)
            {
                output += "thousand";
            }
            else
            {
                output += "thousands";
            }

            if (currentPart < parts - 1)
            {
                output += ", ";
            }
            else if (currentPart == parts - 1)
            {
                output += " and ";
            }
        }

        if (hundreds > 0)
        {
            currentPart++;

            output += hundreds + " ";

            if (hundreds == 1)
            {
                output += "hundred";
            }
            else
            {
                output += "hundreds";
            }

            if (currentPart < parts - 1)
            {
                output += ", ";
            }
            else if (currentPart == parts - 1)
            {
                output += " and ";
            }
        }

        if (tens > 0)
        {
            output += tens + " ";

            if (tens == 1)
            {
                output += "ten";
            }
            else
            {
                output += "tens";
            }
        }

        if (output.equals(""))
        {
            output = "0 tens";
        }

        System.out.println(output);
    }
}
    

