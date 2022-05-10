public class SDES 
{
    //public static int key[] = {1,0,1,0,0,0,0,0,1,0};
    public static int key[] = {1,0,1,0,1,0,1,0,1,0};

    // Used for suffle
    public static int[] IP = { 2, 6, 3, 1, 4, 8, 5, 7 };
    public static int[] EP = { 4, 1, 2, 3, 2, 3, 4, 1 };
    public static int[] P4 = { 2, 4, 3, 1 };
    public static int P10[] = { 3, 5, 2, 7, 4, 10, 1, 9, 8, 6 };
    public static int P8[] = { 6, 3, 7, 4, 8, 5, 10, 9 };
    public static int[] IP_inv = { 4, 1, 3, 5, 7, 2, 8, 6 };

    public static int[][] S0 = { { 1, 0, 3, 2 },
                                { 3, 2, 1, 0 },
                                { 0, 2, 1, 3 },
                                { 3, 1, 3, 2 } };       
    public static int[][] S1 = { { 0, 1, 2, 3 },
                                { 2, 0, 1, 3 },
                                { 3, 0, 1, 0 },
                                { 2, 1, 0, 3 } };

    public static int key1[] = new int[8];
    public static int key2[] = new int[8];
    
    public static void print(int []arr)
    {
        for(int i : arr)
            System.out.print(i);
        System.out.println();
    }
    
    // Used to do left shift by n
    public static int[] shift(int []arr , int n)
    {
        int shifted[] = new int[5];
        int i=0;
        for(; i<5-n; i++)
        {
            shifted[i] = arr[i+n];
        }
        for(; i<5; i++)
        {
            shifted[i] = arr[i-arr.length+n];
        }
        return shifted;
    }
    
    // Used to convert 0123 to binary
    public static String calc(int val)
    {
        if(val == 0)
            return "00";
        else if(val == 1)
            return "01";
        else if(val == 2)
            return "10";
        else 
            return "11";
    }
    
    // First & Last 4 elements gets swapped (Its for 8 bits)
    public static int[] swapping(int []arr)
    {
        for(int i=0; i<4; i++)
        {
            int temp = arr[i];
            arr[i] = arr[i+4];
            arr[i+4] = temp;
        }
        return arr;
    }
    
    // For Key Generation 
    public static void keyGeneration()
    {
        int []temp = new int[10];
        // P10 Permutation of key
        for(int i=0; i<10; i++)
        {
            temp[i] = key[P10[i]-1];
        }
        
        int l[] = new int[5];
        int r[] = new int[5];
        for(int i=0; i<5; i++)
        {
            l[i] = temp[i];
            r[i] = temp[i+5];
        }
        
        // left shift both l & r by 1
        l = shift(l,1);
        r = shift(r,1);
        for(int i=0; i<5; i++)
        {
            temp[i] = l[i];
            temp[i+5] = r[i];
        }
        
        // P8 Permutation to get key1
        for(int i=0; i<8; i++)
        {
            key1[i] = temp[P8[i] - 1];
        }
        
        // left shift both l & r by 2
        l = shift(l,2);
        r = shift(r,2);
        for(int i=0; i<5; i++)
        {
            temp[i] = l[i];
            temp[i+5] = r[i];
        }
        
        // P8 Permutation to get key2
        for(int i=0; i<8; i++)
        {
            key2[i] = temp[P8[i] - 1];
        }
        
        // Printing Key1 & Key2
        System.out.print("Your first key is : ");
        print(key1);
        System.out.print("Your second key is : ");
        print(key2);
        
    }
    
    public static int[] funtionK(int []text , int[] key)
    {
        int l[] = new int[4];
        int r[] = new int[4];
        for(int i=0;i<4;i++)
        {
            l[i] = text[i];
            r[i] = text[i+4];
        }
        
        // Ep permutation (Expanding r from 4 to 8)
        int ep[] = new int[8];
        for(int i=0;i<8;i++)
        {
            ep[i] = r[EP[i]-1];
        }
        
        // xoring ep & key to get text
        for(int i=0; i<8; i++)
        {
            text[i] = ep[i] ^ key[i];
        }
        
        int l1[] = new int[4];
        int r1[] = new int[4];
        for(int i=0;i<4;i++)
        {
            l1[i] = text[i];
            r1[i] = text[i+4];
        }
        
        // first & last bits for row
        // And middle two bits for col
        int row = Integer.parseInt(""+l1[0]+l1[3] , 2);
        int col = Integer.parseInt(""+l1[1]+l1[2] , 2);
        int val = S0[row][col];
        String ls = calc(val);
        
        row = Integer.parseInt(""+r1[0]+r1[3] , 2);
        col = Integer.parseInt(""+r1[1]+r1[2] , 2);
        val = S1[row][col];
        String rs = calc(val);
        
        int[] new_r = new int[4];
        new_r[0] = Character.getNumericValue(ls.charAt(0));
        new_r[1] = Character.getNumericValue(ls.charAt(1));
        new_r[2] = Character.getNumericValue(rs.charAt(0));
        new_r[3] = Character.getNumericValue(rs.charAt(1));
        
        // P4 permutation on new_r
        for(int i=0; i<4; i++)
        {
            new_r[i] = new_r[P4[i]-1];
        }
        
        // xoring l & new_r 
        for(int i=0; i<4; i++)
        {
            l[i] = l[i] ^ new_r[i];
        }
        
        // Combining
        int newText[] = new int[8];
        for(int i=0;i<4;i++)
        {
            newText[i]=l[i];
            newText[i+4]=r[i];
        }
        return newText;
    }
                
    public static int[] encryption(int plain_text[])
    {
        int permute[] = new int[8];
        // IP permutation
        for(int i=0; i<8; i++)
        {
            permute[i] = plain_text[IP[i]-1];
        }
        
        // functionK (key-1) - swapping - functionK (key-2)
        permute = funtionK(permute , key1);
        permute = swapping(permute);
        permute = funtionK(permute , key2);
        
        // IP_inv permutation
        int cipher_text[] = new int[8];
        for(int i=0; i<8; i++)
        {
            cipher_text[i] = permute[IP_inv[i]-1];
        }
        return cipher_text;
    }
    
    public static int[] decryption(int cipher_text[])
    {
        int permute[] = new int[8];
        // IP permutation
        for(int i=0; i<8; i++)
        {
            permute[i] = cipher_text[IP[i]-1];
        }
        
        // functionK (key-2) - swapping - functionK (key-1)
        permute = funtionK(permute , key2);
        permute = swapping(permute);
        permute = funtionK(permute , key1);
        
        // IP_inv permutation
        int decrypted_text[] = new int[8];
        for(int i=0; i<8; i++)
        {
            decrypted_text[i] = permute[IP_inv[i]-1];
        }
        return decrypted_text;
    }
    
    public static void main(String args[]) 
    {
        // Key Generation
        //int plain_text[] = {1,0,0,1,0,1,1,1};
        int plain_text[] = {1,0,1,0,1,0,1,0};
        keyGeneration();
        
        //Encryption
        int cipher_text[] = encryption(plain_text);
        System.out.print("Cipher Text: ");
        print(cipher_text);
        
        //Decryption
        int decrypted_text[] = decryption(cipher_text);
        System.out.print("Decrypted Text: ");
        print(decrypted_text);
    }
}
// 10010111