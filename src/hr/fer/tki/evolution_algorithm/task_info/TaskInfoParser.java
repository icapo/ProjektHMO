package hr.fer.tki.evolution_algorithm.task_info;

import hr.fer.tki.evolution_algorithm.chromosome.IChromosome;
import hr.fer.tki.evolution_algorithm.chromosome.TableChromosome;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskInfoParser {
	
	private static String emptyShiftSign = " ";

	public static TaskInfo parse(String filepath) {
		// Open the file
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		TaskInfo taskInfo = new TaskInfo();
		String line;

		// Read File Line By Line
		try {
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || line.length() == 0) {
					continue;
				}

				if (line.startsWith("SECTION_HORIZON")) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}

						taskInfo.setNumberOfDays(Integer.parseInt(line));
					}
				}

				if (line.startsWith("SECTION_SHIFTS")) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}

						String[] elems = line.split(",");
						String shiftID = elems[0];
						int lengthInMinutes = Integer.parseInt(elems[1]);

						taskInfo.addShift(shiftID, new Shift(shiftID,
								lengthInMinutes));

						if (elems.length == 3 && elems[2].contains("|")) {
							elems = elems[2].split("\\|");
							for (int i = 0; i < elems.length; i++) {
								taskInfo.getShift(shiftID).addCannotFollow(
										taskInfo.getShift(elems[i]));
							}
						}
					}
				}

				if (line.startsWith("SECTION_STAFF")) {
					int employeeIndex = 0;
					while ((line = br.readLine()) != null) {
						if (line.startsWith("#")) {
							employeeIndex = 0;
							continue;
						}
						if (line.length() == 0) {
							break;
						}

						String[] elems = line.split(",");
						String employeeID = elems[0];
						int maxTotalMins = Integer.parseInt(elems[2]);
						int minTotalMins = Integer.parseInt(elems[3]);
						int maxConsShifts = Integer.parseInt(elems[4]);
						int minConsShifts = Integer.parseInt(elems[5]);
						int minConsDaysOff = Integer.parseInt(elems[6]);
						int maxWeekends = Integer.parseInt(elems[7]);

						EmployeeInfo employee = new EmployeeInfo(employeeID,
								employeeIndex, maxTotalMins, minTotalMins,
								maxConsShifts, minConsShifts, minConsDaysOff,
								maxWeekends);

						if (elems[1].contains("|")) {
							elems = elems[1].split("\\|");
							for (int i = 0; i < elems.length; i++) {
								String[] equals = elems[i].split("=");
								employee.putMaxShift(
										taskInfo.getShift(equals[0]),
										Integer.parseInt(equals[1]));
							}
						}
						taskInfo.addStaff(employeeID, employee);
						employeeIndex++;
					}
				}

				if (line.startsWith("SECTION_DAYS_OFF")) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}

						String[] elems = line.split(",");
						String employeeID = elems[0];

						for (int i = 1; i < elems.length; i++) {
							taskInfo.getStaff(employeeID).addDayOff(
									Integer.parseInt(elems[i]));
						}
					}
				}

				if (line.startsWith("SECTION_SHIFT_ON_REQUESTS")) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}

						String[] elems = line.split(",");
						String employeeID = elems[0];
						int dayIndex = Integer.parseInt(elems[1]);
						String shiftID = elems[2];
						int weight = Integer.parseInt(elems[3]);

						ShiftRequest shiftReq = new ShiftRequest(dayIndex,
								shiftID, weight);

						taskInfo.getStaff(employeeID).addShiftOnRequest(
								shiftReq);
					}
				}

				if (line.startsWith("SECTION_SHIFT_OFF_REQUESTS")) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}

						String[] elems = line.split(",");
						String employeeID = elems[0];
						int dayIndex = Integer.parseInt(elems[1]);
						String shiftID = elems[2];
						int weight = Integer.parseInt(elems[3]);

						ShiftRequest shiftReq = new ShiftRequest(dayIndex,
								shiftID, weight);

						taskInfo.getStaff(employeeID).addShiftOffRequest(
								shiftReq);
					}
				}

				if (line.startsWith("SECTION_COVER")) {
					while ((line = br.readLine()) != null) {
						if (line.startsWith("#")) {
							continue;
						}
						if (line.length() == 0) {
							break;
						}

						String[] elems = line.split(",");
						int dayIndex = Integer.parseInt(elems[0]);
						String shiftID = elems[1];
						int requirement = Integer.parseInt(elems[2]);
						int weightUnder = Integer.parseInt(elems[3]);
						int weightOver = Integer.parseInt(elems[4]);

						taskInfo.addCover(new Cover(dayIndex, shiftID,
								requirement, weightUnder, weightOver));
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return taskInfo;
	}

	public static void write(IChromosome chrom, String filepath) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(filepath, "UTF-8");

			for (int row = 0; row < chrom.getRowsNum(); row++) {
				for (int col = 0; col < chrom.getColsNum(); col++) {
					String elem = (String) chrom.getChromosomeElement(row, col);
					if (elem != null) {
						writer.print(chrom.getChromosomeElement(row, col));
					} else {
						writer.print(emptyShiftSign);
					}
					if (col != chrom.getColsNum() - 1) {
						writer.print("	");
					}
				}
				writer.print("\n");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static IChromosome parseResult(String filepath) {
		// Open the file
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String line;

		List<String[]> pomList = new ArrayList<String[]>();
		
		// Read File Line By Line
		try {
			while ((line = br.readLine()) != null) {
				String[] elems = line.split("\t");
				pomList.add(elems);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (pomList.size() == 0 || pomList.get(0) == null) {
			System.out.println("File is not in valid format!");
			System.exit(1);
		}
		
		IChromosome chrom = new TableChromosome(pomList.size(), pomList.get(0).length);
		for (int row = 0; row < pomList.size(); row++) {
			String[] elems = pomList.get(row);
			for (int col = 0; col < elems.length; col++) {
				if (elems[col].equals(emptyShiftSign)) {
					chrom.setChromosomeElement(row, col, null);
				} else {
					chrom.setChromosomeElement(row, col, elems[col]);
				}
			}
		}
		return chrom;
	}

}
