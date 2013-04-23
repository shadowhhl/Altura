package view;

import static view.ViewConst.comboWidthOffset;
import static view.ViewConst.getNPVTableColNum;
import static view.ViewConst.getNPVTableTitle;
import static view.ViewConst.getNPVTableTitleWidth;
import static view.ViewConst.getNPVTableTitleX;
import static view.ViewConst.getNPVTableTitleY;
import static view.ViewConst.getStatsTableTitleX;
import static view.ViewConst.mainWindowHeight;
import static view.ViewConst.mainWindowWidth;
import static view.ViewConst.npvCalColumnWidth;
import static view.ViewConst.npvCalHeight;
import static view.ViewConst.npvCalTitle;
import static view.ViewConst.npvCalWidth;
import static view.ViewConst.npvGroupControlHeights;
import static view.ViewConst.npvGroupControlNames;
import static view.ViewConst.npvGroupControlWidth;
import static view.ViewConst.npvGroupControlX;
import static view.ViewConst.npvGroupControlYs;
import static view.ViewConst.npvParamHeight;
import static view.ViewConst.npvParamLabelWidth;
import static view.ViewConst.npvParamTextWidth;
import static view.ViewConst.npvParamsEditable;
import static view.ViewConst.npvParamsNames;
import static view.ViewConst.npvStatsHeight;
import static view.ViewConst.npvStatsLabelWidth;
import static view.ViewConst.npvStatsNames;
import static view.ViewConst.npvStatsTextWidth;
import static view.ViewConst.outputGroupControlHeights;
import static view.ViewConst.outputGroupControlNames;
import static view.ViewConst.outputGroupControlWidth;
import static view.ViewConst.outputGroupControlX;
import static view.ViewConst.outputGroupControlYs;
import static view.ViewConst.outputNPVTableHeight;
import static view.ViewConst.outputNPVTableTitleHeights;
import static view.ViewConst.outputNPVTableTitleRowNum;
import static view.ViewConst.outputNPVTableTitleWidths_0;
import static view.ViewConst.outputNPVTableXOffset;
import static view.ViewConst.outputStatsTableHeight;
import static view.ViewConst.outputStatsTableTitleHeight;
import static view.ViewConst.outputStatsTableTitles;
import static view.ViewConst.outputStatsTableWidths;
import static view.ViewConst.outputStatsTableXOffset;
import static view.ViewConst.statsGroupControlHeights;
import static view.ViewConst.statsGroupControlNames;
import static view.ViewConst.statsGroupControlWidth;
import static view.ViewConst.statsGroupControlX;
import static view.ViewConst.statsGroupControlYs;
import static view.ViewConst.statsNpvGroupControlHeights;
import static view.ViewConst.statsNpvGroupControlWidths;
import static view.ViewConst.statsNpvGroupControlXs;
import static view.ViewConst.statsNpvGroupControlYs;
import static view.ViewConst.statsNpvTableColumnWidth;
import static view.ViewConst.statsNpvTableHeight;
import static view.ViewConst.statsNpvTableNames;
import static view.ViewConst.statsNpvTableWidth;
import static view.ViewConst.statsParamHeight;
import static view.ViewConst.statsParamLabelWidth;
import static view.ViewConst.statsParamTextWidth;
import static view.ViewConst.statsParamsEditable;
import static view.ViewConst.statsParamsNames;
import static view.ViewConst.tabFolderNames;
import static view.ViewConst.updateButtonHeight;
import static view.ViewConst.updateButtonWidth;
import static view.ViewConst.updateButtonX;
import static view.ViewConst.updateButtonY;
import static view.ViewConst.updateString;
import static view.ViewConst.userguideGroupControlHeights;
import static view.ViewConst.userguideGroupControlNames;
import static view.ViewConst.userguideGroupControlWidth;
import static view.ViewConst.userguideGroupControlX;
import static view.ViewConst.userguideGroupControlYs;
import static view.ViewConst.userguideSessions;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import model.ContentController;
import model.Formater;
import model.ParamList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import portfolio.Portfolio;

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
	
	public static ParamList paramList;
	
	public Portfolio arcPortfolio;
	
	private Text squareFootageConst; 
	
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
		initialParamList();
		
		shell = new Shell(SWT.CLOSE);
		shell.setSize(mainWindowWidth, mainWindowHeight);
		shell.setText("Altura Consulting");
		shell.setBackground(getColor(SWT.COLOR_WHITE));
		
		int tabFolderWidth = mainWindowWidth;
		int tabFolderHeight = mainWindowHeight - 20;
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.setBounds(0, 0, tabFolderWidth, tabFolderHeight);
		
		for (int i=0;i<tabFolderNames.length;i++) {
			if (i==1 || i==2 || i==4 || i==5 || i==6) {
				continue;
			}
			TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
			tbtmNewItem.setText(tabFolderNames[i]);
			createTabItem(tabFolder, tbtmNewItem, i);
		}
		
	}
	
	private void initialParamList() {
		paramList = new ParamList();
		//today's date
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		paramList.setParamValue(npvParamsNames[0], sdf.format(date));
		//WACC (%)
		paramList.setParamValue(npvParamsNames[1], String.valueOf(10.0));
		//Monthly WACC (%)
		paramList.setParamValue(npvParamsNames[2], String.valueOf(10.0/12.0));
		//Maintenance Costs (%)
		paramList.setParamValue(npvParamsNames[3], String.valueOf(7.0));
		//Transaction Costs (%)
		paramList.setParamValue(npvParamsNames[4], String.valueOf(8.0));
		//Tax Costs (%)
		paramList.setParamValue(npvParamsNames[5], String.valueOf(30.0));
		//Annual Rental Growth Rate (%)
		paramList.setParamValue(npvParamsNames[6], String.valueOf(4.0));
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
				lblNewLabel.setBounds(5, 5, userguideGroupControlWidth, userguideGroupControlHeights[i]);
				lblNewLabel.setText(userguideSessions[i]);
				Display display = Display.getDefault();
				Font lblFont = new Font(display, "Lucida", 12, java.awt.Font.PLAIN);
				lblNewLabel.setFont(lblFont);
				
				if (i==userguideGroupControlNames.length-1) {
					Composite imgComposite = new Composite(grpText, SWT.NONE);
					Display imgDisplay = Display.getDefault();
					ImageData fcImageData = new ImageData("FlowChartForUser.jpg");
					fcImageData = fcImageData.scaledTo(850, 510);
					Image flowChartImage = new Image(imgDisplay, fcImageData);
					imgComposite.setBounds(40, 0, fcImageData.width, fcImageData.height);
					imgComposite.setBackgroundImage(flowChartImage);
				}
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
		case 6: { //creating the dynamic pricing page
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
					//System.out.println(paramList);

					TableItem[] tItems =  statsNpvTable.getItems();
					
					ArrayList<String[]> updatedContent = new ArrayList<String[]>();
					ContentController cc = new ContentController();
					updatedContent = cc.getUpdateStatsNpvTableContent(arcPortfolio, currentSelectedLoanIndex, paramList);
					for (int j=0;j<updatedContent.size();j++) {
						//System.out.println(updatedContent.get(j));
						tItems[j].setText(updatedContent.get(j));
					}
					
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
							if (key.equals("WACC (%)")) {
								String waccStr = text.getText();
								Double wacc = Double.valueOf(waccStr);
								Double monthWacc = 100.0*(Math.pow(1+wacc/100.0, 1.0/12.0)-1.0);
								paramList.setParamValue("Monthly WACC (%)", monthWacc.toString());
								
								Control[] controls = text.getParent().getChildren();
								for (int i=0;i<controls.length;i++) {
									String names = (String)controls[i].getData("id");
									if (names!=null) {
										if (names.equals("Monthly WACC (%)")) {
											Text tmpText = (Text)controls[i];
											
											tmpText.setText(Formater.toShortDouble(monthWacc.doubleValue(), 2));
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
						
						for (int i=0;i<=tableContents.size();i++) {
							if (i>0) {
								TableItem tItem = new TableItem(portfolioTable, SWT.NONE);
								tItem.setText(tableContents.get(i-1));
							} else {
								TableItem tItem = new TableItem(portfolioTable, SWT.NONE);
								int zEstColIndex=0, todayEstColIndex=0, npvValueColIndex=0;
								for (int index=0;index<npvCalTitle.length;index++) {
									if (npvCalTitle[index].equalsIgnoreCase("Zillow Estimate")) zEstColIndex=index;
									if (npvCalTitle[index].equalsIgnoreCase("Today's Est. Price")) todayEstColIndex=index;
									if (npvCalTitle[index].equalsIgnoreCase("NPV Value")) npvValueColIndex=index;
								}
								Double zEstimate = 0.0, todayEstPrice = 0.0, npvValue=0.0;
								for (int ti=0;ti<tableContents.size();ti++) {
									String[] tRow = tableContents.get(ti);
									if (!tRow[zEstColIndex].equals("N/A")) 
										zEstimate += Double.valueOf(Formater.currencyToString(tRow[zEstColIndex]));
									if (!tRow[todayEstColIndex].equals("N/A")) 
										todayEstPrice += Double.valueOf(Formater.currencyToString(tRow[todayEstColIndex]));
									if (!tRow[npvValueColIndex].equals("N/A")) 
										npvValue += Double.valueOf(Formater.currencyToString(tRow[npvValueColIndex]));
								}
								String zEsStr = Formater.toCurrency(zEstimate);
								String tEsStr = Formater.toCurrency(todayEstPrice);
								String npvValStr = Formater.toCurrency(npvValue);
								String[] firstLine = new String[npvCalTitle.length];
								for (int index=0;index<npvCalTitle.length;index++) {
									if (index==0) firstLine[index]="Portfolio";
									else if (index==zEstColIndex) firstLine[index]=zEsStr;
									else if (index==todayEstColIndex) firstLine[index]=tEsStr;
									else if (index==npvValueColIndex) firstLine[index]=npvValStr;
									else firstLine[index]="";
								}
								tItem.setText(firstLine);
							}
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
			
			Label vLabel = new Label(group, SWT.NONE);
			vLabel.setBounds(350, 10, 150, 20);
			vLabel.setText("NPV Valuation Base");
			
			final Combo valueSelection = new Combo(group, SWT.BORDER);
			valueSelection.setBounds(500, 10, 150, 30);
			valueSelection.add("Zillow Price");
			valueSelection.add("Altura Price");
			valueSelection.select(0);
			paramList.setParamValue("Value Option", "zillow");
			valueSelection.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					int valueOption = valueSelection.getSelectionIndex();
					if (valueOption==0) {
						paramList.setParamValue("Value Option", "zillow");
					} else {
						paramList.setParamValue("Value Option", "altura");
					}
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
			break;
		}
		case 1: {
			int[] displayOptions = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_NPV_STATS};
			defaultContent = cc.getParamContent(displayOptions, null);
			
			for (int i=0;i<npvStatsNames.length;i++) {
				Label label = new Label(group, SWT.NONE);
				
				label.setBounds(10, i*(npvStatsHeight+5)+10, npvStatsLabelWidth, npvStatsHeight);
				label.setText(npvStatsNames[i]);
				
				if (i==1) { //square footage constraint
					squareFootageConst = new Text(group, SWT.NONE);
					squareFootageConst.setBounds(npvStatsLabelWidth+20, i*(npvStatsHeight+5)+10, npvStatsTextWidth, npvStatsHeight);
					squareFootageConst.setText(defaultContent.get(i));
					squareFootageConst.setEditable(true);
					squareFootageConst.setBackground(getColor(SWT.COLOR_WHITE));
					
				} else {
					Text text = new Text(group, SWT.NONE);
					text.setBounds(npvStatsLabelWidth+20, i*(npvStatsHeight+5)+10, npvStatsTextWidth, npvStatsHeight);
					text.setText(defaultContent.get(i));
					text.setEditable(false);
					text.setBackground(getColor(SWT.COLOR_GRAY));
				}
			}
			break;
		}
		case 2: {
			int[] displayOptions = {ContentController.DISPLAY_DEFAULT, ContentController.DISPLAY_NPV_TABLE};
			ArrayList<String[]> tableContents = cc.getTableContent(displayOptions, arcPortfolio);
			portfolioTable = new Table(group, SWT.BORDER|SWT.FULL_SELECTION);
			portfolioTable.setHeaderVisible(true);
			portfolioTable.setLinesVisible(true);
			portfolioTable.setBounds(10, 10, npvCalWidth, npvCalHeight);

			portfolioTable.addListener(SWT.DefaultSelection, new Listener() {
				
				@Override
				public void handleEvent(Event e) {
					try {
						
						ContentController cc = new ContentController();
						int selectionIndex = portfolioTable.getSelectionIndex();
						if (selectionIndex!=0) {
							//get row in the table
							TableItem row = portfolioTable.getItem(selectionIndex);
							//get loan account
							String accountNum = row.getText(0);
							//get property
							HashMap<String, String> selectedRow = arcPortfolio.getEntry(accountNum);
							//get size constraint constraint
							Double sizeConst = Double.valueOf(squareFootageConst.getText()) / 100.0;
							
							Integer numBedrooms = Integer.valueOf(selectedRow.get("#Bedrooms"));
							Double dNum = Double.valueOf(selectedRow.get("#Bathrooms"));
							Integer numBathrooms = dNum.intValue();
							Double size = Double.valueOf(selectedRow.get("Size/SqFeet"));
							String zipCode = selectedRow.get("Zip Code");
							String type = selectedRow.get("Type");
							String closedSqlStmt = null, activeSqlStmt = null;
							
							if (type.equalsIgnoreCase("SFR")) {
								closedSqlStmt = cc.sqlGenerator(ContentController.SQL_COMPARABLE_LIST_CSONLY, 
									numBedrooms, numBathrooms, size, sizeConst, zipCode, ContentController.PROPERTY_TYPE_SFR);
								activeSqlStmt = cc.sqlGenerator(ContentController.SQL_COMPARABLE_LIST_AONLY, 
										numBedrooms, numBathrooms, size, sizeConst, zipCode, ContentController.PROPERTY_TYPE_SFR);
							} else {
								closedSqlStmt = cc.sqlGenerator(ContentController.SQL_COMPARABLE_LIST_CSONLY, 
										numBedrooms, numBathrooms, size, sizeConst, zipCode, ContentController.PROPERTY_TYPE_CONDO);
								activeSqlStmt = cc.sqlGenerator(ContentController.SQL_COMPARABLE_LIST_AONLY, 
										numBedrooms, numBathrooms, size, sizeConst, zipCode, ContentController.PROPERTY_TYPE_CONDO);
							}
							
							ArrayList< HashMap<String, String> > comparableClosedProperties = cc.getSqlResult(closedSqlStmt);
							ArrayList< HashMap<String, String> > comparableActiveProperties = cc.getSqlResult(activeSqlStmt);
							
							PricePlotWindow lpList = new PricePlotWindow();
							lpList.setClosedPriceData(comparableClosedProperties);
							lpList.setActivePriceData(comparableActiveProperties);
							lpList.open();
						}
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
