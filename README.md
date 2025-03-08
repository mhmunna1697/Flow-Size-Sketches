# **Flow-Size Sketches Implementations (CountMin, CountSketch, and Active Counter)**

## **Overview**
This repository contains Java implementations of three probabilistic data structures used for efficient flow size estimation and counting in streaming data:

- **CountMin Sketch**: Estimates flow sizes with minimal space, allowing overestimation but no underestimation.
- **CountSketch**: Uses randomized sign hashing to provide unbiased flow size estimates.
- **Active Counter**: A probabilistic counter that dynamically scales using an exponent to handle large counts efficiently.

Each implementation processes flow data and computes estimation accuracy using real-world input.

---

## **How It Works**

### **1. CountMin Sketch**
- Uses **multiple hash functions** to map flows to counters in a **2D array**.
- Records the **minimum value** among hashed counters for flow size estimation.
- Computes **average error** between estimated and actual flow sizes.
- Outputs **top 100 largest flows** based on estimated size.

### **2. CountSketch**
- Similar to CountMin, but assigns a **random ±1 sign** to each counter update.
- Uses **median estimation** across hashed values to reduce bias.
- Computes **average error** between estimated and actual flow sizes.
- Outputs **top 100 largest flows** based on estimated size.

### **3. Active Counter**
- Uses **32-bit space**, divided into:
  - **16-bit counter** (tracks counts up to a threshold).
  - **16-bit exponent** (scales probabilistically when count overflows).
- Increments **1,000,000 times** with probabilistic updates.
- Outputs the **final estimated counter value**.

---

## **Input Format**
All flow-based implementations (`CountMin`, `CountSketch`) read input from `project2input.txt`, formatted as:

```
10000  # Number of flows
192.168.1.1 777
10.0.0.5 9
... (more flows)
```
- First line: **Number of flows**.
- Each subsequent line: `<FlowID> <PacketCount>`.

Active Counter **does not require input**; it performs **1,000,000 probabilistic increments**.

---

## **Compilation & Execution**

### **Compile All Files**
```bash
javac CountMinSketch.java CountSketch.java ActiveCounter.java
```

### **Run CountMin Sketch**
```bash
java CountMinSketch
```

### **Run CountSketch**
```bash
java CountSketch
```

### **Run Active Counter**
```bash
java ActiveCounter
```

---

## **Output Format**
Each execution generates an output file:
- **`countMin_output.txt`**  → CountMin results
- **`countSketch_output.txt`** → CountSketch results
- **`activeCounter_output.txt`** → Active Counter results


### **Example CountMin / CountSketch Output (`countMin_output.txt`)**
```
Average Error: 121.80   # Average error
104.17.183.118 10490 9956   # FlowID, Estimated Size, True Size
187.237.14.200 10331 9747 
...
```

### **Example Active Counter Output**
```
Final value of the Active Counter: 1001872.0
```

---

## **Performance Considerations**
| **Method**     | **Memory Efficiency** | **Accuracy** | **Bias Handling** |
|---------------|---------------------|-------------|-----------------|
| CountMin Sketch | ✅ High               | 🔸 Overestimates  | ❌ No bias correction |
| CountSketch   | ✅ High               | ✅ Balanced | ✅ Handles bias |
| Active Counter | ✅ Compact           | 🔸 Approximate | ✅ Scales efficiently |

---

## **Future Improvements**
- **Optimize hashing** for better distribution.
- **Enhance accuracy** by fine-tuning parameters.
- **Parallelize updates** for high-speed processing.
- **Implement dynamic scaling** in Active Counter.

---
