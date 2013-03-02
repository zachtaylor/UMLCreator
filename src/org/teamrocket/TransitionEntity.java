package org.teamrocket;

public class TransitionEntity {
	
	private String _input;
	private String _action;
	private StateEntity _next;
	private StateEntity _prev;
	

	public TransitionEntity(){

		
	}
	
	public void setNext(StateEntity n){
		_next= n;
	}
		
	public StateEntity getNext(){
		return _next;
		
	}
	
	public void setPrev(StateEntity p){
		_prev= p;
	}
	
	public StateEntity getPrev(){
		return _prev;
		
	}
	
    public void setInput(String s){
		this._input = s;
	}
    
    public String getInput(){
		return _input;
    	
    }
    
   public void setAction(String a){
		this._action=a;
	}
   
   public String getAction(){
	 return _action;
		
	}


}
