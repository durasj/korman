package me.duras.korman;

import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author mmiskov
 */
public class NewAgentController {
    
    private String category;
    private String series;
    private String size;
    private int idAgenta;
    private String wmn;
    private int minPrice;
    private int maxPrice;
    private int difference;
    private int year;
    private String name;
    private String timeStamp;
    private static boolean onEdit;
    
    private static String cat, ser, sz, wm, min, max, diff, yr, nm;

    //after click button Create Agent >> 
    AgentsDao agentsDao = new AgentsDao();
    
    @FXML
    private TextField setMinPrice, setMaxPrice, setDiff, setYear, setSeries, nameAgent;
    
    @FXML
    private ComboBox<String> setCategory, setSize, forWoman;
    
    @FXML
    private Button createAgentButton;
    
    @FXML
    private AnchorPane newAgent;
    
    @FXML
    private void addAgent(ActionEvent event) {
        
        String outputAgentName = nameAgent.getText();
        if (outputAgentName.equals("")) {
            this.name = "default";
        } else {
            this.name = outputAgentName;
        }
        
        String outputMinPrice = setMinPrice.getText();
        if (outputMinPrice.equals("")) {
            this.minPrice = 0;
        } else {
            this.minPrice = Integer.parseInt(outputMinPrice);
        }
        
        String outputMaxPrice = setMaxPrice.getText();
        if (outputMaxPrice.equals("")) {
            this.maxPrice = Integer.MAX_VALUE;
        } else {
            this.maxPrice = Integer.parseInt(outputMaxPrice);
        }
        
        String outputSetDiff = setDiff.getText();
        if (outputSetDiff.equals("")) {
            this.difference = 0;
        } else {
            this.difference = Integer.parseInt(outputSetDiff);
        }
        
        String outputSetYear = setYear.getText();
        if (outputSetYear.equals("")) {
            this.year = 0;
        } else {
            this.year = Integer.parseInt(outputSetYear);
        }
        
        String outputSetSeries = setSeries.getText();
        if (outputSetSeries.equals("")) {
            series = null;
        } else {
            this.series = outputSetSeries;
        }
        
        String outputSetCategory = setCategory.getSelectionModel().getSelectedItem();
        
        String outputSetSize = setSize.getSelectionModel().getSelectedItem();
        this.size = outputSetSize;
        
        String outputForWoman = forWoman.getSelectionModel().getSelectedItem();
        this.wmn = outputForWoman;
        
        if (!onEdit) {
            agentsDao.createAgent(category, series, size, idAgenta, wmn, minPrice, maxPrice, difference, year, name, timeStamp);
        } else {
            agentsDao.editAgent(category, series, size, idAgenta, wmn, minPrice, maxPrice, difference, year, name, timeStamp);
        }
        
        createAgentButton.setText("Create");
        onEdit = false;
        try {
            
            AnchorPane pane = FXMLLoader.load(getClass().getResource("AgentsTable.fxml"));
            newAgent.getChildren().setAll(pane);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void onEdit(boolean onEdit) {
        this.onEdit = onEdit;
    }
    
    @FXML
    private void initialize() {
        if (onEdit) {
            createAgentButton.setText("Edit");
            System.out.println("Ahoj som zapnuty");
            
            System.out.println(minPrice);
            nameAgent.setText(nm);
            setMinPrice.setText(min);
            setMaxPrice.setText(max);
            setDiff.setText(diff);
            setYear.setText(yr);
            setSeries.setText(ser);
            setCategory.getSelectionModel().select(cat);
            setSize.getSelectionModel().select(sz);
            forWoman.getSelectionModel().select(wm);
        }
    }
    
    public void setName(String nm) {
        this.nm = nm;
    }
    
    public void setCat(String cat) {
        this.cat = cat;
    }
    
    public void setSer(String ser) {
        this.ser = ser;
    }
    
    public void setSz(String sz) {
        this.sz = sz;
    }
    
    public void setWm(String wm) {
        this.wm = wm;
    }
    
    public void setMin(String min) {
        this.min = min;
    }
    
    public void setMax(String max) {
        this.max = max;
    }
    
    public void setDiff(String diff) {
        this.diff = diff;
    }
    
    public void setYr(String yr) {
        this.yr = yr;
    }
    
}
