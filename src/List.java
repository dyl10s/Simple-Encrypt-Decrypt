import java.io.Serializable;
import java.util.NoSuchElementException;

/******************************************************************************
 * A List encapsulates a list of Objects, such as String, etc.
 * 
 * A List is created to have both a top dummy Node and a bottom dummy Node.
 * Cursor is initialized to match the top field. 
 * 
 * The end field is always a reference to the bottom dummy Node.
 * 
 * The convention that is implemented by this List structure goes as follows. 
 * For each value of the cursor field, 
 * 
 *                               cursor
 * 
 * the data are stored in the next Node,
 * 
 *                            cursor.next.data
 * 
 * -----------------------------------------
 *     An empty list
 * Below, cursor = top, i.e. the cursor index is 0
 * 
 * top.prev (cursor.next)   null
 * top.data (cursor.next)   dummy
 * top.next (cursor.next)   link forward
 *                 
 * end.prev                 link backward
 * end.data                 dummy
 * end.next                 null
 * 
 * 
 * 
 * -----------------------------------------
 *     A list of 1 element
 * Below, cursor = top, i.e. the cursor index is 0
 * 
 * top.prev (cursor.next)   null
 * top.data (cursor.next)   dummy
 * top.next (cursor.next)   link forward
 *                 
 * cursor.next.prev         links backward
 * cursor.next.data         "one"
 * cursor.next.next         link forward
 *                 
 * end.prev                 link backward
 * end.data                 dummy
 * end.next                 null
 * 
 * -------------------------------------------
 *     A list of 2 elements
 * Below, cursor = top.next, i.e. the cursor index is 1
 * 
 * top.prev                 null
 * top.data                 dummy
 * top.next                 link forward
 *                 
 * cursor.prev              link backward
 * cursor.data              "one"
 * cursor.next              link forward
 *                 
 * cursor.next.prev         link backward
 * cursor.next.data         "two"
 * cursor.next.next         link forward
 *                 
 * end.prev                 link backward
 * end.data                 dummy
 * end.next                 null
 * 
 * -------------------------------------------
 * 
 * For the initial cursor value, an item will be entered in the
 * 0th position in the list. Unless the cursor is changed, a 
 * subsequent item will be entered still in the 0th position, 
 * where each existing item in the list will be demoted by one place.
 * 
 **********************************************************************/

public class List <T > implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected Node <T> top;     // A top dummy Node to begin the list
    protected Node <T> end;     // An end dummy Node to end the list

    protected Node <T> cursor;  // The current Node in the list. 
    protected int index;
    protected String title;

    /*****************************************************************
     * Invokes the other constructor.
     *****************************************************************/
    public List( )
    {
        this( "" );
    }

    /*****************************************************************
     * Constructs the list with two dummy Nodes, 
     * one Node to always lead the list, and a
     * second Node to always trail the list.
     * 
     * The lead node contains the title;
     *****************************************************************/
    public List( String title )
    {
        this.title = title;
        this.top = new Node <T> ( null, null, null );    // dummy top Node
        this.end = new Node <T> ( null, this.top, null ); // dummy bottom Node
        this.top.next = this.end;

        this.cursor = this.top;
        this.index  = 0;

        //this.setTitle( title );
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getTitle(  )
    {
        return title;
    }

    /*****************************************************************
     * Returns whether the list is empty.
     *****************************************************************/
    public boolean isEmpty( ) 
    {
        return this.top.next.next == null;
    }

    /*****************************************************************
     * Moves the cursor to the top, so that the first item in the
     * list is dereferenced as cursor.next.data
     *****************************************************************/
    public void cursorToFirst( )
    {
        this.cursor = this.top;
    }

    /*****************************************************************
     * Moves the cursor to the end of the list, which is at 
     * 
     *                    this.end.prev.
     *                    
     * No element is ever at the end of the list. Whenever an element
     * is added to the end of the list, it becomes the last element.
     *****************************************************************/
    protected void cursorToEnd( )
    {
        this.cursor = this.end.prev;
    }

    /*****************************************************************
     * Moves the cursor to the Node immediately preceding the
     * trailing Node of the list.
     * 
     * No element is ever at the end of the list. Whenever an element
     * is added to the end of the list, it becomes the last element.
     *****************************************************************/
    protected void cursorToLast( ) 
    {
        this.cursorToEnd( );
        if (! this.cursorIsFirst( ))  //cursor.hasPrevious( )
        {
            this.cursorToPrevious( );
        }
    }

    /*****************************************************************
     * Returns whether the list stores a next item.
     *****************************************************************/
    public boolean hasNext( ) 
    {
        return this.cursor.hasNext( );  //  return this.cursor.next.next != null;
    }

    /*****************************************************************
     * Inserts a Node for the element in the specified index place.
     *****************************************************************/
    public void add( int index, T element )
    {     
        if (0 <= index && index <= this.size( ))
        {        
            this.cursorToIndex( index );
            this.add( element );
        }
    }

    /*****************************************************************
     * inserts a Node for the element immediately after the Node
     * referenced by cursor.  The value of cursor does not change!
     *****************************************************************/
    public void add( T element ) 
    { 
        this.cursor.next = new Node <T> ( element, this.cursor, this.cursor.next );
        this.cursor.next.next.prev = this.cursor.next;
    }

    /******************************************************************
     * 
     ******************************************************************/
    public void addAll(int index, List <T> c)
    {
        if (0 <= index && index <= this.size( ))
        {
            c.reverse( );

            c.cursorToFirst( );
            while (c.hasNext( ) )
            {
                this.add( index, c.getNext( ) );
            }

            c.reverse( );
        }
    }

    /*****************************************************************
     * Inserts a Node for the element immediately after the top
     * dummy node to top the list.
     *****************************************************************/
    public void prefix( T element )
    { 
        this.top.next = new Node <T> ( element, this.top, this.top.next );
        this.top.next.next.prev = this.top.next;
    }

    /*****************************************************************
     * No element is ever at the end of the list. Whenever an element
     * is added to the end of the list, it becomes the last element.
     *
     *
     * This append method inserts a Node for the element at the end of
     * the list, which, physically, will immediately come before the
     * end (dummy) node.
     *****************************************************************/
    public void append( T element )
    {       
        Node <T> anchor = this.cursor;      // saves cursor

        this.cursorToEnd( );
        this.add( element );

        this.cursor = anchor;           // restores cursor
    }

    /*****************************************************************
     * This set method replaces the object, obj, to the current.next
     * node data field.
     *****************************************************************/
    public void set( T obj )
    {
        if (! this.cursorIsAtTheEnd())  // or cursor.hasNext( )
        {
            this.cursor.next.data = obj;
        }
    }

    /*******************************************************************
     * If index is a legitimate list position, this set method 
     * moves the cursor to the specified index position, before
     * invoking the set Object method to complete the object
     * replacement.
     *****************************************************************/
    public void set( int index, T obj )
    {
        Node <T> anchor = this.cursor;      // saves cursor

        if (0 <= index && index <= this.size( ))
        {
            this.cursorToIndex( index );     
            this.set( obj );
        }

        this.cursor = anchor;           // restores cursor
    }

    /*****************************************************************
     * Returns the index of temp in the list for a first occurrence;
     * or -1 if temp is not contained in the list.
     * 
     *****************************************************************/
    public int indexOf( T temp )
    {
        Node <T> anchor = this.cursor;      // saves the cursor

        this.cursorToFirst( );
        int index = 0;
        while (! this.cursorIsAtTheEnd( ))
        {
            if (this.cursor.next.data.equals( temp ))
            {
                this.cursor = anchor;   // restores the cursor
                return index;
            }
            this.cursorToNext( );
            index++;
        }

        this.cursor = anchor;           // restores the cursor
        return -1;
    }

    /*****************************************************************
     * Returns the index of obj for its last occurrence in the list ;
     * or -1 if obj is not contained in the list.
     * 
     * contains ?
     *****************************************************************/
    public int lastIndexOf(T obj)
    {
        Node <T> anchor = this.cursor;           // saves the cursor

        int index = this.size()-1;

        this.cursorToLast( );
        while (! cursor.hasPrevious())  // ! this.isFirst()
        {
            if (this.cursor.next.data.equals( obj ))
            {
                this.cursor = anchor;   // restores the cursor
                return index;
            }

            this.cursorToPrevious( );
            index++;
        }

        this.cursor = anchor;           // restores the cursor
        return -1;
    }

    /*****************************************************************
     * The get method returns the item, cursor.next.getData()
     *****************************************************************/
    public T get( ) 
    {
        T obj = null;
        if (!this.isEmpty( ) && !this.cursorIsAtTheEnd( ))
        {
            obj = this.cursor.next.data;
        }
        else
        {
            System.out.println( "Exception: no data to get at the end of the list." );
        }
        return obj;
    }

    /*****************************************************************
     * get moves the cursor to the specified index position, 
     * and then invokes get() to return the item.
     *****************************************************************/
    public T get( int index )
    {
        Node <T> anchor = this.cursor;      // saves cursor

        this.cursorToIndex( index );  
        T obj = this.get( );

        this.cursor = anchor;           // restores cursor
        return obj;
    }

    /*****************************************************************
     * Returns the current object, and advances the cursor.
     *****************************************************************/
    public T getNext( ) 
    {
        T item = null;
        try
        {
            if (!cursor.hasNext())
            {
                throw new Exception( "No element at the end of the list." );
            }
            cursor = cursor.next;
            item = cursor.data;
        }
        catch (Exception e )
        {

        }
        return item;
    }

    /*****************************************************************
     * Returns the first element in the list.
     *****************************************************************/
    public T getFirst( )
    {
        return this.get( 0 );
    }

    /*****************************************************************
     * Returns the last element in the list.
     *****************************************************************/
    public T getLast( )
    {
        Node <T> anchor = this.cursor;      // saves cursor

        if (this.isEmpty( ))
        {
            return null;
        }

        this.cursorToLast( );
        T obj = this.get( );

        this.cursor = anchor;           // restores cursor
        return obj;
    }

    /*******************************************************************
     * Returns a sub-list of elements from start to stop in this list.
     * Elements in the returned sub-list are shared by this list.
     ******************************************************************/
    public List <T> subList(int start, int stop)
    {
        List <T> newList = new List <T> (  );

        if (start < 0 || start > stop || stop > this.size( ))
        {
            throw new IllegalArgumentException( );
        }   

        this.cursorToIndex( start );

        while (! this.cursorIsAtTheEnd() && start <= stop)
        {
            newList.append( this.get( ) );

            this.cursorToNext();
            start++;
        }

        return newList;
    }

    /*****************************************************************
     * Swaps two elements in the list, the element at index j
     * with the element at index k.
     *****************************************************************/
    public void swap( int j, int k )
    {
        Node < T > anchor = this.cursor;      // saves the cursor

        T temp = this.get( j );    
        this.set( j, this.get( k ) );
        this.set( k, temp );

        this.cursor = anchor;           // restores the cursor
    }

    /*****************************************************************
     * Empties the list.
     *****************************************************************/
    public void clearAll( )
    {
        this.cursorToFirst( );

        try
        {
            while (! this.isEmpty( ))
            {
                this.remove( );
            }
        }
        catch (Exception e)
        {
            System.out.println( "Exception. Attempt to remove from an empty list." );
        }
    }

    /*****************************************************************
     * Reverses the order of the list.
     *****************************************************************/
    public void reverse( )
    {
        Node < T > anchor = this.cursor;      // saves the cursor

        this.cursorToIndex( 1 );
        while ( ! this.cursorIsAtTheEnd( ) )
        {
            try
            {
                this.prefix( this.remove( ) );
            }
            catch (Exception e)
            {
            }
        }

        this.cursor = anchor;           // restores the cursor
    }

    /*****************************************************************
     * Returns the value, curror.next.data, and re-links the list to exclude
     * the node that contained the data.  The value of cursor does not change!
     * 
     * @return
     * @throws Exception
     *****************************************************************/
    public T remove( )
    {
        T obj = null;

        if (cursor.hasNext())
        {
            obj = this.cursor.next.data;
            this.cursor.next      = this.cursor.next.next;
            this.cursor.next.prev = this.cursor;
        }

        return obj;
    }

    /*****************************************************************
     * First moves the cursor to the index specified location in the list,
     * and then calls this.remove to remove and return the value.
     * @return
     * @throws Exception
     *****************************************************************/
    public T remove( int index )
    {
        Node <T> anchor = this.cursor;     // saves the cursor

        T obj = null;
        try
        {
            this.cursorToIndex( index );
            obj = this.remove( );
        }
        catch (Exception e)
        {
            System.out.println( "Exception: attempt to remove an item beyond on the end of the list" );
        }

        this.cursor = anchor;           // restores the cursor
        return obj;
    }

    /*****************************************************************
     * Returns a count of how many elements are contained within the list.
     *****************************************************************/
    public int size( )
    {
        Node <T> anchor = this.cursor;     // saves the cursor

        int count = 0;
        for (this.cursorToFirst( ); ! this.cursorIsAtTheEnd( ); this.cursorToNext() )
        {
            count++;
        }

        this.cursor = anchor;           // restores the cursor
        return count;
    }

    /*
     * Returns an iterator to the start of the invoking list.
     */
    //     public ListIterator listIterator( )
    //     {
    //     }

    /*
     * Returns an iterator to the invoking list that begins at the specified index.
     */

    //     public ListIterator listIterator(int index)
    //     {
    //     }

    /*****************************************************************
     * Returns the index for the cursor position in the list.
     *****************************************************************/
    public int getIndex( )
    {
        Node <T> anchor = this.cursor;      // saves the cursor

        int index = 0;
        this.cursor = this.top;

        while (this.cursor != anchor)
        {
            this.cursorToNext( );
            index++;
        }

        this.cursor = anchor;           // restores the cursor
        return index;
    }

    /*****************************************************************
     * Returns a string for all of the elements in the list from 
     * top to bottom.
     *****************************************************************/
    public String toString( )
    {
        Node < T > anchor = this.cursor;     // saves the cursor

        String returnString = ""; // this.top.data.toString( );
        for (this.cursorToFirst( ); ! this.cursorIsAtTheEnd(); this.cursorToNext( )) 
        {
            returnString += this.get( ).toString( );
        }

        this.cursor = anchor;           // restores the cursor
        return returnString;
    }

    /*****************************************************************
     * Returns a string for the elements in the list 
     * indexed between start and stop, inclusively.
     *****************************************************************/
    public String display( int start, int stop )
    {
        Node <T> anchor = this.cursor;     // saves the cursor  

        String returnString;

        if (start < 0 || start > stop || stop > this.size( ))
        {
            throw new IllegalArgumentException( );
        }   

        this.cursorToIndex( start );
        returnString = "";
        while (! this.cursorIsAtTheEnd() && start <= stop)
        {
            returnString += this.get( );

            this.cursorToNext();
            start++;
        }

        this.cursor = anchor;           // restores the cursor
        return returnString;
    }

    /*****************************************************************
     * Changes the cursor to reference the Node specified by the index parameter.
     *****************************************************************/
    protected void cursorToIndex( int index )
    {
        if (0 <= index && index <= this.size( ))
        {
            this.cursorToFirst( );
            while ( index > 0 && !this.cursorIsAtTheEnd( ))
            {
                this.cursorToNext( );
                index--;
            }
        }
    }

    /*****************************************************************
     * Advances the cursor to the next place in the list.
     *****************************************************************/
    protected void cursorToNext( )
    {
        //        if (! this.cursorIsAtTheEnd( ))      //this.cursor.next.next != null) 
        if (cursor.hasNext( ))
        {       
            this.cursor = this.cursor.next;
        }
    }

    /*****************************************************************
     * Backs up the cursor to the previous place in the list.
     *****************************************************************/
    protected void cursorToPrevious( )
    {
        if (cursor.hasPrevious( ))  //! this.cursorIsFirst( )) or this.cursor.prev != null) 
        {          
            this.cursor = this.cursor.prev;
        }
    }

    /*****************************************************************
     * Returns whether the cursor points to
     * the 0th location in the list
     *****************************************************************/
    public boolean cursorIsFirst( )
    {
        return this.cursor == this.top;
    }

    /*****************************************************************
     * Returns whether the cursor points to
     * the last occupied location in the list
     *****************************************************************/
    protected boolean cursorIsLast( )
    {
        return this.isEmpty( ) || this.cursor.next.next.next == null;
    }

    /*****************************************************************
     * Returns whether the cursor points to
     * the end of the list, i.e. the location
     * for appending an item to the list.
     *****************************************************************/
    protected boolean cursorIsAtTheEnd( )
    {
        return this.cursor.next.next == null;  // this.cursor.next.next == this.end;
    }

    /*****************************************************************
     *****************************************************************/
    //     public void print( )
    //     {
    //         Node anchor = this.cursor;     // saves the cursor
    // 
    //         for ( int n = 0; n < this.size(); n++ )
    //         {
    //             System.out.print( " " + n );
    //         }
    //         System.out.println( );
    // 
    //         String str;
    //         for ( int n = 0; n < this.size(); n++ )
    //         {
    //             str = this.get(n).toString( );
    //             if ( n < 10)
    //             {
    //                 System.out.print( " " + this.get(n).toString( ) );
    //             }
    //             else
    //             {
    //                 System.out.print( "  " + this.get(n).toString( ) );
    //             }
    //         }
    //         System.out.println( );
    // 
    //         this.cursor = anchor;           // restores the cursor
    //     }
    private class  Node <T> implements Serializable
    {
        static final long serialVersionUID = 0;

        public Node <T> prev;     // a reference to the previous node in the chain
        public T data;   // the data stored in this node 
        public Node <T> next;     // a reference to the next node in the chain

        public Node( )
        {
            this( null, null, null );
        }

        public Node( T data, Node < T > prev, Node < T > next )
        {
            this.data = data; 
            this.prev = prev;
            this.next = next; 
        }

        /***************************************************************
         * This constructor is to be used for singly-linked structures
         ***************************************************************/
        public Node( T data, Node < T > next )
        {
            this.data = data; 
            this.next = next; 

            this.prev = null;
        }

        public boolean equals( Object obj )
        {
            try {
                T o = (T) obj; 
                return this.data.equals( o );

            }
            catch ( ClassCastException ce ) {
                System.out.println("Casting caused an exception.");
            }
            return false;
        }

        public boolean hasPrevious( )
        {
            return this.prev != null;
        }

        public boolean hasNext( )
        {
            return this.next.next != null;
        }

        public String toString( )
        {
            return this.data.toString( );
        }

        // public static void main( String[ ] args )
        // {
        // Node <String > node = new Node( "String type data", null, null );
        // System.out.println( node.toString( ) );

        // Node <Integer> node2 = new Node( new Integer( 17 ), null, null );
        // System.out.println( node2.toString( ) );
        // }
    }
    /*****************************************************************
     * 
     * 
     *****************************************************************/
    public static void main( String[ ] args )
    {
        List <String > list = new List <String> (  );

        list.add( "one" );
        list.add( "two" );
        list.add( "three" );

        System.out.println( list.toString( ) );

        list.reverse( );
        //list.setTitle( "List reversed: " );
        System.out.println( list.toString( ) );

        list.reverse( );
        //list.setTitle( "List reversed twice: " );
        System.out.println( list.toString( ) );
    }
}