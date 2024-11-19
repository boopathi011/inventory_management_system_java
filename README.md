### **README**  

#### **Project Title**  
**Java-Based Inventory Management System** 🗄️📊  

---

#### **Overview**  
This project is a desktop-based inventory management system built using Java Swing and MySQL. It allows users to manage two inventories: a main inventory (`inv`) and an additional inventory (`inv1`). The system supports operations such as adding, updating, searching, and deleting items from the inventories. Additionally, users can view the final inventory, which calculates the differences between the two inventories for reconciliation purposes.  

---

#### **Features** ✨  
1. **Database-Driven System** 📂  
   - Data is stored and retrieved from a MySQL database for persistent storage.  
2. **Dual Inventory Management** 🛒  
   - Separate handling for main (`inv`) and additional (`inv1`) inventories.  
3. **CRUD Operations** 🛠️  
   - Add, update, search, and delete inventory items.  
4. **Final Inventory Calculation** 📈  
   - Automatically compute the final inventory by reconciling the main and additional inventories.  
5. **Real-Time Data Display** 📋  
   - Results are displayed in an easy-to-read tabular format using `JTable`.  
6. **Error Handling** ⚠️  
   - Validation and meaningful error messages for better user experience.  

---

#### **Technologies Used** 🖥️  
- **Programming Language**: Java ☕  
- **GUI Framework**: Java Swing 🎨  
- **Database**: MySQL 🛢️  
- **Database Connector**: JDBC (Java Database Connectivity) 🔌  

---

#### **Setup Instructions** 🔧  

1. **Prerequisites**:  
   - Install [Java JDK](https://www.oracle.com/java/technologies/javase-jdk-downloads.html) (version 8 or later) ✔️.  
   - Install [MySQL](https://dev.mysql.com/downloads/) and set up a local database 🛠️.  
   - Install an IDE like [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/) 🖋️.  

2. **Database Setup**:  
   - Create a database named `inventory` 🗄️.  
   - Create two tables:  
     ```sql  
     CREATE TABLE inv (  
         item_name VARCHAR(255) PRIMARY KEY,  
         item_quantity INT NOT NULL  
     );  

     CREATE TABLE inv1 (  
         item_name VARCHAR(255) PRIMARY KEY,  
         item_quantity INT NOT NULL  
     );  
     ```  
   - Adjust `DB_USER` and `DB_PASSWORD` in the code to match your MySQL credentials 🔑.  

3. **Running the Project** 🚀:  
   - Compile and run the `InventoryManagement` Java file using your IDE or command line:  
     ```bash  
     javac InventoryManagement.java  
     java InventoryManagement  
     ```  
   - The GUI will appear, allowing you to interact with the system.  

4. **Usage** 🛒:  
   - Use the input fields to specify item names and quantities.  
   - Perform actions like adding, updating, searching, or removing items from the inventory using the respective buttons 🔘.  
   - View the final inventory using the "Show Final Inventory" button 📊.  

---

#### **Future Enhancements** 🚀  
- Add user authentication for secure access 🔒.  
- Support for exporting inventory data to Excel or CSV 📂.  
- Implement a web-based version for remote access 🌐.  
- Introduce graphical reports for inventory trends 📉📊.  

---

#### **Contributing** 🤝  
Contributions are welcome! Please follow these steps:  
1. Fork the repository 🍴.  
2. Create a new branch for your feature or bug fix 🌱.  
3. Commit your changes and create a pull request 📥.  

---

#### **License** 📜  
This project is released under the MIT License 📝.
