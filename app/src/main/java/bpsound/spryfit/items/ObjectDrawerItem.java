package bpsound.spryfit.items;

/**
 * Created by kyunghopark on 15. 4. 27..
 */
public class ObjectDrawerItem {
    public int icon;
    public String name;
    public int numMsg;

    public ObjectDrawerItem(int icon, String name){
        this.icon = icon;
        this.name = name;
    }

    public void setIcon(int ic){
        this.icon = ic;
    }

    public int getIcon(){
        return this.icon;
    }

    public String getTitle(){
        return this.name;
    }

    public void setNewMsgNum(int num){
        this.numMsg = num;
    }

    public int getNewMsgNum(){
        return this.numMsg;
    }
}
