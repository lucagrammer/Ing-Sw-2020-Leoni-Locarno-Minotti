package client.guiComponents;

import client.GuiView;
import model.Cell;
import util.Genre;
import util.MapInfo;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapElement {
    private PPanelContainer bodyContainer;
    private GuiView guiView;

    private PPanelContainer commandBar;
    private PPanelContainer keyContainer;
    private PPanelContainer mapContainer;
    private PLabelError errorLabel;
    private PLabel commandLabel;
    private PButton[][] cells;

    public MapElement(PPanelContainer bodyContainer, GuiView guiView){
        this.bodyContainer= bodyContainer;
        this.guiView=guiView;

        Image islandImg = (new ImageIcon(getClass().getResource("/GuiResources/SantoriniBoard.png"))).getImage();
        JPanel island = new PPanelBackground(islandImg);
        island.setBounds(0,50+30,496,500);
        island.repaint();
        island.setLayout(null);
        this.bodyContainer.add(island);

        mapContainer= new PPanelContainer();
        mapContainer.setBounds(70,63,368,368);
        mapContainer.setLayout(new GridLayout(5,5,11,11));
        island.add(mapContainer);

        errorLabel = new PLabelError("");
        errorLabel.setBounds(0, 50, 840, 25);
        this.bodyContainer.add(errorLabel);

        PPanelContainer sideBar= new PPanelContainer();
        sideBar.setBounds(island.getWidth()+20,63+30,324,450);
        sideBar.setLayout(new GridLayout(2,2,15,15));
        this.bodyContainer.add(sideBar);

        commandBar= new PPanelContainer();
        commandBar.setBounds(30,103,154,220);
        commandBar.setLayout(new GridLayout(2,2,20,20));
        sideBar.add(commandBar);

        keyContainer= new PPanelContainer();
        keyContainer.setBounds(30,103,sideBar.getWidth(),175);
        keyContainer.setLayout(new GridLayout(1,3,5,5));
        sideBar.add(keyContainer);

    }

    public void showMap(MapInfo mapInfo){
        cells= new PButton[5][5];
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                cells[i][j] = new PButton(mapInfo,i,j);
                cells[i][j].setSize(30,65);
                mapContainer.add(cells[i][j]);
            }
        }
    }

    public void setHeading(String heading){
        commandLabel = new PLabel(heading);
        commandLabel.setFontSize(30);
        commandLabel.setBounds(0, 0, bodyContainer.getWidth(), 40);
        bodyContainer.add(commandLabel);
    }

    public void enableCellSelection(List<Cell> forbiddenCells, Genre genre) {

        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                Cell currentCell= new Cell(i,j);
                if(forbiddenCells.contains(currentCell)){
                    cells[i][j].addActionListener((ev) -> (new Thread(() -> {
                        addErrorLabel("Invalid choice. Try again.");
                    })).start());
                }else{
                    cells[i][j].addActionListener((ev) -> (new Thread(() -> {
                        guiView.getServerHandler().sendPlayerPosition(genre, currentCell);
                    })).start());
                }
            }
        }
    }

    public void addErrorLabel(String errorMessage){
        errorLabel.setText(errorMessage);
    }

}
