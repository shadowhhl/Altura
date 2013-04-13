package view;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import static view.ViewConst.*;
import model.*;
import portfolio.*;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

public class MainWindow {

	protected Shell shell;
	public Table outputNPVTable;
	public Table outputStatsTable;
	private Table portfolioTable;
	
	private Table statsNpvTable;
	private Composite outputImageComposite;
	private Composite senImageComposite;
	private Composite statsImageComposite0;
	private Composite statsImageComposite1;
	private Button npvUpdateButton;
	private int currentSelectedLoanIndex = 0;
	
	private ParamList paramList;
	
	public Portfolio arcPortfolio;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			MainWindow window = new MainWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
				
			}
		}
		
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		initialPortfolio();
		doCalculation();
		initialParams();
		
		shell = new Shell(SWT.CLOSE);
		shell.setSize(mainWindowWidth, mainWindowHeight);
		shell.setText("Altura Consulting");
		shell.setBackground(getColor(SWT.COLOR_WHITE));
		
		int tabFolderWidth = mainWindowWidth;
		int tabFolderHeight = mainWindowHeight - 20;
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0, tabFolderWidth, tabFolderHeight);
		
		for (int i=0;i<tabFolderNames.length;i++) {
			if (tabFolderNames[i].equals("Sensitivity")) {
				continue;
			}
			TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
			tbtmNewItem.setText(tabFolderNames[i]);
			createTabItem(tabFolder, tbtmNewItem, i);
		}
		
	}
	
	private void initialParams() {
		paramList = new ParamList();
		//today's date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		paramList.setParamValue(npvParamsNames[0], sdf.format(date));
		//IRR (%)
		paramList.setParamValue(npvParamsNames[1], String.valueOf(10.0));
		//Monthly IRR (%)
		paramList.setParamValue(npvParamsNames[2], String.valueOf(100.0*(Math.pow(1+10.0/100.0, 1.0/12.0)-1.0)));
		//Inflation Rate (%)
		paramList.setParamValue(npvParamsNames[3], String.valueOf(2.5));
		//Maintenance Costs (%)
		paramList.setParamValue(npvParamsNames[4], String.valueOf(8.0));
		//Transaction Costs (%)
		paramList.setParamValue(npvParamsNames[5], String.valueOf(7.0));
	}
	
	private void doCalculation() {
		//Read from database
		
		//Statistical Model
		
		//Npv Price
		
		
	}

	private void createTabItem(TabFolder tabFolder, TabItem tbtmItem, int tabIndex) {
		switch (tabIndex) {
		case 0: { //creating the user guide page
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmItem.setControl(composite);
			for (int i=0;i<userguideGroupControlNames.length;i++) {
				Group grpText = new Group(composite, SWT.NONE);
				grpText.setText(userguideGroupControlNames[i]);
				grpText.setBounds(userguideGroupControlX, 
								  userguideGroupControlYs[i], 
								  userguideGroupControlWidth,
								  userguideGroupControlHeights[i]);
				
				Label lblNewLabel = new Label(grpText, SWT.NONE);
				lblNewLabel.setBounds(0, 0, userguideGroupControlWidth, userguideGroupControlHeights[i]);
				lblNewLabel.setText(userguideSessions[i]);
			}
			break;
		}
		case 1: { //creating the output(1) page
			ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.NONE);
			tbtmItem.setControl(scrolledComposite);
			scrolledComposite.setExpandHorizontal(true);
			scrolledComposite.setExpandVertical(true);
			
			Composite composite = new Composite(scrolledComposite, SWT.NONE);
			
			for (int i=0;i<outputGroupControlNames.length-1;i++) {
				Group group = new Group(composite, SWT.NONE);
				group.setText(outputGroupControlNames[i]);
				group.setBounds(outputGroupControlX, 
						  		outputGroupControlYs[i], 
						  		outputGroupControlWidth,
						  		outputGroupControlHeights[i]);
				createOutputGroup(group, i);
			}
			
			scrolledComposite.setContent(composite);
			scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			break;
		}
		case 2: { //creating the output(2) page
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmItem.setControl(composite);
			
			Group group = new Group(composite, SWT.NONE);
			int height = 0; for (int i=0;i<outputGroupControlHeights.length;i++) height+=outputGroupControlHeights[i]; 
			group.setBounds(outputGroupControlX, outputGroupControlYs[0], 
					outputGroupControlWidth, height);
			createOutputGroup(group, 2);
			break;
		}
		case 3: { //creating the portfolio npv page
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmItem.setControl(composite);
			
			for (int i=0;i<npvGroupControlNames.length;i++) {
				Group group = new Group(composite, SWT.NONE);
				group.setText(npvGroupControlNames[i]);
				group.setBounds(npvGroupControlX, 
						  		npvGroupControlYs[i], 
						  		npvGroupControlWidth,
						  		npvGroupControlHeights[i]);
				createNpvGroup(group, i);
			}
			break;
		}
//		case 4: { //creating the sensitivity page
//			Composite composite = new Composite(tabFolder, SWT.NONE);
//			tbtmItem.setControl(composite);
//			
//			for (int i=0;i<senGroupControlNames.length;i++) {
//				Group group = new Group(composite, SWT.NONE);
//				group.setText(senGroupControlNames[i]);
//				group.setBounds(senGroupControlX, 
//						  		senGroupControlYs[i], 
//						  		senGroupControlWidth,
//						  		senGroupControlHeights[i]);
//				createSenGroup(group, i);
//			}
//		}
//		break;
		case 5: { //creating the statistical model page
			Composite composite = new Composite(tabFolder, SWT.NONE);
			tbtmItem.setControl(composite);
			
			for (int i=0;i<statsGroupControlNames.length;i++) {
				Group group = new Group(composite, SWT.NONE);
				group.setText(statsGroupControlNames[i]);
				group.setBounds(statsGroupControlX, 
						  		statsGroupControlYs[i], 
						  		statsGroupControlWidth,
						  		statsGroupControlHeights[i]);
				createStatsGroup(group, i);
			}
			break;
		}
		default: {
			break;
		}
		}
	}

	private void createStatsGroup(Group group, int index) {
		ContentController cc = new ContentController();
		switch (index) {
		case 0: {
			ArrayList<String> defaultContents = new ArrayList<String>();
			int[] displayOptions = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_STATS_PARAMS};
			defaultContents = cc.getParamContent(displayOptions, null);
			for (int i=0;i<statsParamsNames.length;i++) {
				Label label = new Label(group, SWT.NONE);
				label.setBounds(10, i*(statsParamHeight+5), 
								statsParamLabelWidth, statsParamHeight);
				label.setText(statsParamsNames[i]);
				
				if (i==0) {
					final Combo combo = new Combo(group, SWT.BORDER);
					combo.setBounds(10+statsParamLabelWidth+10, 
									i*(statsParamHeight+5), 
									statsParamTextWidth+comboWidthOffset, 
									statsParamHeight+11);
					ArrayList<String> contents = new ArrayList<String>();
					int[] options = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_ALL_ACOUNT_NUM};
					contents = cc.getParamContent(options, arcPortfolio);
					String[] comboList = new String[contents.size()];
					contents.toArray(comboList);
					combo.setItems(comboList);
					combo.select(currentSelectedLoanIndex);
					combo.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent arg0) {
							// TODO Auto-generated method stub
							currentSelectedLoanIndex = combo.getSelectionIndex();
						}
					});
				}
				else {
					Text text = new Text(group, SWT.NONE);
					text.setBounds(10+statsParamLabelWidth+10, 
								   i*(statsParamHeight+5), 
								   statsParamTextWidth, 
								   statsParamHeight);
					text.setEditable(statsParamsEditable[i]);
					text.setText(defaultContents.get(i));
					if (statsParamsEditable[i]==true)
						text.setBackground(getColor(SWT.COLOR_WHITE));
					else
						text.setBackground(getColor(SWT.COLOR_GRAY));
					
				}
			}
			Button buttonUpdate = new Button(group, SWT.BORDER);
			buttonUpdate.setText(updateString);
			buttonUpdate.setBounds(updateButtonX, updateButtonY, updateButtonWidth, updateButtonHeight);
			buttonUpdate.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
//					System.out.println(paramList);
//
//					TableItem[] tItems =  statsNpvTable.getItems();
//					
//					ArrayList<String[]> updatedContent = new ArrayList<String[]>();
//					ContentController cc = new ContentController();
//					updatedContent = cc.getUpdateStatsNpvTableContent(arcPortfolio, currentSelectedLoanIndex, paramList);
//					for (int j=0;j<updatedContent.size();j++) {
//						System.out.println(updatedContent.get(j));
//						tItems[j].setText(updatedContent.get(j));
//					}
//					
//					ArrayList<ArrayList<Double>> prices = new ArrayList<ArrayList<Double>>();
//					ArrayList<Double> bestEstimate = new ArrayList<Double>();
//					ArrayList<Double> ourPrice = new ArrayList<Double>();
//					ArrayList<Double> zillowPrice = new ArrayList<Double>();
//					
//					//for (int j=0;j<upd)
					
				}
			});
			break;
		}
		case 1: {
			ArrayList<String[]> defaultContents = new ArrayList<String[]>();
			int[] displayOptions = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_STATS_NPV_TABLE};
			defaultContents = cc.getTableContent(displayOptions, null);
			
			for (int i=0;i<statsNpvGroupControlXs.length;i++) {
				Composite composite = new Composite(group, SWT.NONE);
				composite.setBounds(statsNpvGroupControlXs[i], 
									statsNpvGroupControlYs[i], 
								    statsNpvGroupControlWidths[i], 
								    statsNpvGroupControlHeights[i]);
				if (i==0) {  //create table
					statsNpvTable = new Table(composite, SWT.BORDER|SWT.FULL_SELECTION);
					statsNpvTable.setHeaderVisible(true);
					statsNpvTable.setLinesVisible(true);
					statsNpvTable.setBounds(0, 0, statsNpvTableWidth, statsNpvTableHeight);

					for (int j=0;j<statsNpvTableNames.length;j++) {
						TableColumn tableColumn = new TableColumn(statsNpvTable, SWT.NONE);
						tableColumn.setText(statsNpvTableNames[j]);
						tableColumn.setWidth(statsNpvTableColumnWidth[j]);
					}
					
					for (int j=0;j<defaultContents.size();j++) {
						TableItem tItem = new TableItem(statsNpvTable, SWT.NONE);
						tItem.setText(defaultContents.get(j));
					}
				}
				else { //create graph
					
				}
			}
			break;
		}
		}
	}
	
	private void createNpvGroup(Group group, int index) {
		ContentController cc = new ContentController();
		ArrayList<String> defaultContent = new ArrayList<String>();
		switch (index) {
		case 0: {
			int[] displayOptions = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_NPV_PARAMS};
			defaultContent = cc.getParamContent(displayOptions, null);
			for (int i=0;i<npvParamsNames.length;i++) {
				Label label = new Label(group, SWT.NONE);
				final Text text = new Text(group, SWT.NONE);
				label.setBounds(10, i*(npvParamHeight+5)+10, npvParamLabelWidth, npvParamHeight);
				label.setText(npvParamsNames[i]);
				text.setBounds(npvParamLabelWidth+20, i*(npvParamHeight+5)+10, npvParamTextWidth, npvParamHeight);
				text.setEditable(npvParamsEditable[i]);
				text.setData("id", npvParamsNames[i]);
				if (npvParamsEditable[i]==true)
					text.setBackground(getColor(SWT.COLOR_WHITE));
				else
					text.setBackground(getColor(SWT.COLOR_GRAY));
				text.setText(defaultContent.get(i));
				text.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent arg0) {
						try {
							String key = (String)text.getData("id");
							paramList.setParamValue(key, text.getText());
							if (key.equals("IRR (%)")) {
								String irrStr = text.getText();
								Double irr = Double.valueOf(irrStr);
								Double monthIrr = 100.0*(Math.pow(1+irr/100.0, 1.0/12.0)-1.0);
								paramList.setParamValue("Monthly IRR (%)", monthIrr.toString());
								
								Control[] controls = text.getParent().getChildren();
								for (int i=0;i<controls.length;i++) {
									String names = (String)controls[i].getData("id");
									if (names!=null) {
										if (names.equals("Monthly IRR (%)")) {
											Text tmpText = (Text)controls[i];
											
											tmpText.setText(Formater.toShortDouble(monthIrr.doubleValue(), 2));
										}
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			npvUpdateButton = new Button(group, SWT.BORDER);
			MouseListener listener = new MouseListener() {
				
				@Override
				public void mouseUp(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseDown(MouseEvent arg0) {
					// TODO Auto-generated method stub
					try {
						//get items from table
						TableItem[] tItems = portfolioTable.getItems();
						
						ContentController cc = new ContentController();
						int[] displayOptions = {ContentController.DISPLAY_UPDATE, ContentController.DISPLAY_NPV_TABLE};
						ArrayList<String[]> tableContents = cc.getTableContent(displayOptions, arcPortfolio);
						
						Comparator<String[]> contentComparator = new ContentComparator();			
						Collections.sort(tableContents, contentComparator);
						
						for (int i=0;i<tItems.length;i++) {
							tItems[i].dispose();
						}
						for (int i=0;i<tableContents.size();i++) {
							TableItem tItem = new TableItem(portfolioTable, SWT.NONE);
							tItem.setText(tableContents.get(i));
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
				@Override
				public void mouseDoubleClick(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			};
			npvUpdateButton.addMouseListener(listener);
			npvUpdateButton.setText(updateString);
			npvUpdateButton.setBounds(updateButtonX, updateButtonY, updateButtonWidth, updateButtonHeight);
			break;
		}
		case 1: {
			int[] displayOptions = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_NPV_STATS};
			defaultContent = cc.getParamContent(displayOptions, null);
			for (int i=0;i<npvStatsNames.length;i++) {
				Label label = new Label(group, SWT.NONE);
				Text text = new Text(group, SWT.NONE);
				label.setBounds(10, i*(npvStatsHeight+5)+10, npvStatsLabelWidth, npvStatsHeight);
				label.setText(npvStatsNames[i]);
				text.setBounds(npvStatsLabelWidth+20, i*(npvStatsHeight+5)+10, npvStatsTextWidth, npvStatsHeight);
				text.setText(defaultContent.get(i));
				text.setEditable(false);
				text.setBackground(getColor(SWT.COLOR_GRAY));
			}
			break;
		}
		case 2: {
			int[] displayOptions = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_NPV_TABLE};
			ArrayList<String[]> tableContents = cc.getTableContent(displayOptions, arcPortfolio);
			portfolioTable = new Table(group, SWT.BORDER|SWT.FULL_SELECTION);
			portfolioTable.setHeaderVisible(true);
			portfolioTable.setLinesVisible(true);
			portfolioTable.setBounds(0, 0, npvCalWidth, npvCalHeight);

			portfolioTable.addListener(SWT.DefaultSelection, new Listener() {
				
				@Override
				public void handleEvent(Event e) {
					try {
						ContentController cc = new ContentController();
						System.out.println(portfolioTable.getSelectionIndex());
						int selectionIndex = portfolioTable.getSelectionIndex();
						HashMap<String, String> selectedRow = arcPortfolio.getEntry(selectionIndex);
						Integer numBedrooms = Integer.valueOf(selectedRow.get("#Bedrooms"));
						Integer numBathrooms = Integer.valueOf(selectedRow.get("#Bathrooms"));
						Double size = Double.valueOf(selectedRow.get("Size/SqFeet"));
						String zipCode = selectedRow.get("Zip Code");
						String sqlStmt = cc.sqlGenerator(ContentController.SQL_COMPARABLE_LIST_CSONLY, 
								numBedrooms, numBathrooms, size, zipCode, ContentController.PROPERTY_TYPE_SFR);
						ArrayList< HashMap<String, String> > comparableProperties = cc.getSqlResult(sqlStmt);
						//System.out.println(comparableProperties);
						LPList lpList = new LPList();
						lpList.setListPriceData(comparableProperties);
						lpList.open();
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				}
			});
			for (int i=0;i<npvCalTitle.length;i++) {
				TableColumn tableColumn = new TableColumn(portfolioTable, SWT.NONE);
				tableColumn.setText(npvCalTitle[i]);
				tableColumn.setWidth(npvCalColumnWidth);
			}
			
			for (int i=0;i<tableContents.size();i++) {
				TableItem tItem = new TableItem(portfolioTable, SWT.NONE);
				tItem.setText(tableContents.get(i));
			}
			break;
		}
		}
	}
	
	private void createOutputGroup(Group group, int index) {
		ContentController cc = new ContentController();
		switch (index) {
		case 0: {
			for (int row=0;row<outputNPVTableTitleRowNum;row++) {
				for (int col=0;col<getNPVTableColNum(row);col++) {
					Label label = new Label(group, SWT.CENTER);
					
					label.setBounds(getNPVTableTitleX(row, col)+outputNPVTableXOffset,
									getNPVTableTitleY(row),
									getNPVTableTitleWidth(row, col),
									outputNPVTableTitleHeights[row]);
					label.setText(getNPVTableTitle(row, col));
				}
			}
			//create table
			int outputNPVTableY = 0;{for (int i=0;i<outputNPVTableTitleHeights.length;i++) outputNPVTableY+=outputNPVTableTitleHeights[i];}
			int outputNPVTableWidth = 0;{for (int i=0;i<outputNPVTableTitleWidths_0.length;i++) outputNPVTableWidth+=outputNPVTableTitleWidths_0[i];}
			
			outputNPVTable = new Table(group, SWT.BORDER|SWT.FULL_SELECTION);
			outputNPVTable.setHeaderVisible(false);
			outputNPVTable.setLinesVisible(true);
			outputNPVTable.setBounds(0+outputNPVTableXOffset, 
								  	 outputNPVTableY, 
									 outputNPVTableWidth, 
									 outputNPVTableHeight);
			for (int i=0;i<getNPVTableColNum(2);i++) {
				TableColumn tableColumn = new TableColumn(outputNPVTable, SWT.NONE);
				tableColumn.setWidth(getNPVTableTitleWidth(2, i));
			}
			
			ArrayList<String[]> portfolioNpv = new ArrayList<String[]>();
			int[] options = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_OUTPUT_NPV_TABLE};
			portfolioNpv = cc.getTableContent(options, arcPortfolio);
			for (int i=0;i<portfolioNpv.size();i++) {
				TableItem tItem = new TableItem(outputNPVTable, SWT.NONE);
				tItem.setText(portfolioNpv.get(i));
			}
			break;
		}
		case 1: { //create statistical model
			//create loan selection combo 
			Label lsLabel = new Label(group, SWT.NONE);
			lsLabel.setBounds(outputStatsTableXOffset, 0, 
							  outputStatsTableWidths[0], outputStatsTableHeight);
			lsLabel.setText(outputStatsTableTitles[0]);
			
			final Combo lsCombo = new Combo(group, SWT.NONE);
			lsCombo.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			ArrayList<String> contents = new ArrayList<String>();
			int[] options = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_ALL_ACOUNT_NUM};
			contents = cc.getParamContent(options, arcPortfolio);
			String[] comboList = new String[contents.size()];
			contents.toArray(comboList);
			lsCombo.setItems(comboList);
			lsCombo.setBounds(outputStatsTableXOffset+outputStatsTableWidths[0], 
							  0, 
							  outputStatsTableWidths[0], 
							  outputStatsTableHeight);
			//create table
			int outputStatsTableY = 4 * outputStatsTableTitleHeight;
			int outputStatsTableWidth = 0;{for (int i=0;i<outputStatsTableWidths.length;i++) outputStatsTableWidth+=outputStatsTableWidths[i];}
			
			Label numDealslabel = new Label(group, SWT.NONE);
			numDealslabel.setBounds(outputStatsTableXOffset, 
							2*outputStatsTableTitleHeight, 
							outputStatsTableWidths[0], 
							outputStatsTableTitleHeight);
			numDealslabel.setText(outputStatsTableTitles[1]);
			
			for (int i=0;i<outputStatsTableTitles.length-2;i++) {
				Label lblTitle = new Label(group, SWT.CENTER);
				lblTitle.setBounds(outputStatsTableXOffset+getStatsTableTitleX(i), 
							3*outputStatsTableTitleHeight, 
							outputStatsTableWidths[i], 
							outputStatsTableTitleHeight);
				lblTitle.setText(outputStatsTableTitles[i+2]);
			}
			outputStatsTable = new Table(group, SWT.BORDER|SWT.FULL_SELECTION);
			outputStatsTable.setHeaderVisible(false);
			outputStatsTable.setLinesVisible(true);
			outputStatsTable.setBounds(0+outputStatsTableXOffset, 
									   outputStatsTableY, 
									   outputStatsTableWidth, 
									   outputStatsTableHeight);
			for (int i=0;i<outputStatsTableWidths.length;i++) {
				TableColumn tableColumn = new TableColumn(outputStatsTable, SWT.NONE);
				tableColumn.setWidth(outputStatsTableWidths[i]);
			}
			
			ArrayList<String[]> portfolioStat = new ArrayList<String[]>();
			options[0] = ContentController.DISPLAY_DEFAULT;
			options[1] = ContentController.DISPLAY_OUTPUT_STATS_TABLE;
			portfolioStat = cc.getTableContent(options, null);
			for (int i=0;i<portfolioStat.size();i++) {
				TableItem tItem = new TableItem(outputStatsTable, SWT.NONE);
				tItem.setText(portfolioStat.get(i));
			}
			
			break;
			
		}
		case 2: {
			outputImageComposite = new Composite(group, SWT.NONE);
			break;
		}
		}
	}
	
	private void setImage(String imageURL, int index) {
		URL url;
		try {
			url = new URL(imageURL);
			Display imgDisplay = Display.getDefault();
			Image image = new Image(imgDisplay, url.openStream());
			
			switch (index) {
			case 0: {	//the image in Output(2)
				outputImageComposite.setBackgroundImage(image);
				outputImageComposite.setBounds(10, 10, image.getImageData().width, image.getImageData().height);
				break;
			}
			case 1: {	//the image in Sensitivity
				senImageComposite.setBackgroundImage(image);
				senImageComposite.setBounds(10, 10, image.getImageData().width, image.getImageData().height);
				break;
			}
			case 2: {	//the image in Statistical Model 0
				statsImageComposite0.setBackgroundImage(image);
				statsImageComposite0.setBounds(10, 10, image.getImageData().width, image.getImageData().height);
				break;
			}
			case 3: {	//the image in Statistical Model 1
				statsImageComposite1.setBackgroundImage(image);
				statsImageComposite1.setBounds(10, 10, image.getImageData().width, image.getImageData().height);
				break;
			}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Color getColor(int systemColorID) {
		return SWTResourceManager.getColor(systemColorID);
	}

	private void initialPortfolio() {
		arcPortfolio = new Portfolio();
		arcPortfolio.initialPortfolio();
	}
	
}

class ContentComparator implements Comparator<String[]> {

	@Override
	public int compare(String[] arg0, String[] arg1) {
		//get index of "Zillow Estimate"
		int indexZ = 0;
		for (indexZ=0;indexZ<npvCalTitle.length;indexZ++) {
			if (npvCalTitle[indexZ].equals("Zillow Estimate")) break;
		}
		//get index of "today's estimate"
		int indexT = 0;
		for (indexT=0;indexT<npvCalTitle.length;indexT++) {
			if (npvCalTitle[indexT].equals("Today's Est. Price")) break;
		}
		double priceDiff0, priceDiff1;
		String zPriceStr = Formater.currencyToString(arg0[indexZ]);
		String tPriceStr = Formater.currencyToString(arg0[indexT]);
		priceDiff0 = Double.valueOf(zPriceStr)-Double.valueOf(tPriceStr);
		priceDiff0 = Math.abs(priceDiff0);
		
		zPriceStr = Formater.currencyToString(arg1[indexZ]);
		tPriceStr = Formater.currencyToString(arg1[indexT]);
		priceDiff1 = Double.valueOf(zPriceStr)-Double.valueOf(tPriceStr);
		priceDiff1 = Math.abs(priceDiff1);
		
		if (priceDiff0>=priceDiff1)
			return 1;
		else 
			return -1;
	}
	
}
