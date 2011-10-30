package pt.up.fe.cmov.listadapter;


public class EntryItem implements Item{

	public final String title;
	public final String subtitle;
	public int pos;

	public EntryItem(int pos,String title, String subtitle) {
		this.pos = pos;
		this.title = title;
		this.subtitle = subtitle;
	}
	
	@Override
	public boolean isSection() {
		return false;
	}
	
	public int getPos(){
		return pos;
	}

}
