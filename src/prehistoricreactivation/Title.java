/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prehistoricreactivation;

/**
 *
 * @author speedy
 */
class Title implements GameName{
    private String gameName;
    
    public Title(String name){
        
    }
    
    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public void setGameName(String name) {
        gameName = name;
    }
    
}
