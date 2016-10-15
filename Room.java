import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;



public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<String, Item> items;
    private HashMap<String, Monster> monsters;
    //private Monster mymonster;
   
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new HashMap<String, Item>();
        monsters = new HashMap<String, Monster>();
    }

   
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    public void addItem(String code, Item item)
    {  
        items.put(code, item);
    }
    public void addMonster(String name, Monster monster)
    {
        monsters.put(name, monster);
    }
    public void clear()
    {
    monsters.clear();
    items.clear();
    }
     
   
    public String getShortDescription()
    {
        return description;
    }

   
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString();
    }

   
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    public String listItems() 
    {
       String returnString = new
       String("Code\tDescription\n");
       Iterator iter = items.keySet().iterator();
       Item item = null;
       String code = null;
       while (iter.hasNext()) {
       code = (String)iter.next();
       item = (Item)items.get(code);
       returnString += code + '\t' + item.getInfo()  + '\n';
       }
       return returnString;
    }

    public int getMonstersHealth() 
    {
        Monster m1=null;
        int health=0;
        Set keyset= monsters.keySet();
        Iterator<String> itr1=keyset.iterator();
        while(itr1.hasNext()){
        String key1=itr1.next();
        m1=monsters.get(key1);
        }
        if(m1!=null){
        health=m1.getHealth();
        }
        return health;
    }
    public int monstersSize(){
          return monsters.size();
    }
   
}
