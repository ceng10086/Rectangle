# Rectangle 程序测试报告

## 1. 项目概述

本项目对 `Rect` 类中的三个核心方法进行 JUnit 单元测试：
- `getArea()` — 计算矩形面积
- `getPerimeter()` — 计算矩形周长
- `findMax()` — 在数组中找出最大元素

## 2. 测试方法与策略

### 2.1 静态测试（Static Testing）

通过代码审查，在测试前发现以下问题：

| 编号 | 问题描述 | 严重程度 |
|------|---------|---------|
| S1 | `Rect.java` 源文件使用 GBK 编码，Maven 编译（UTF-8）时报错 | 高 |
| S2 | 构造函数和 setter 未校验负数输入，可能导致面积/周长为负 | 中 |
| S3 | `findMax()` 未校验 null 或空数组，会抛出 `NullPointerException` 或 `ArrayIndexOutOfBoundsException` | 高 |
| S4 | `findMax()` 未校验 null 比较器，会抛出 `NullPointerException` | 中 |
| S5 | `getArea()` 使用 `int` 乘法，大数值时存在整数溢出风险 | 低 |

### 2.2 黑盒测试 — 基于输入域（等价类划分 + 边界值分析）

#### getArea() 等价类划分

| 等价类 | 输入描述 | 测试用例 | 预期结果 |
|--------|---------|---------|---------|
| 正常正值 | length=5, width=10 | `Rect(5,10).getArea()` | 50 |
| 单位长度 | length=1, width=10 | `Rect(1,10).getArea()` | 10 |
| 单位宽度 | length=10, width=1 | `Rect(10,1).getArea()` | 10 |
| 正方形 | length=width=7 | `Rect(7,7).getArea()` | 49 |
| 零值长度 | length=0, width=5 | `Rect(0,5).getArea()` | 0 |
| 零值宽度 | length=5, width=0 | `Rect(5,0).getArea()` | 0 |
| 全零 | length=0, width=0 | `Rect(0,0).getArea()` | 0 |
| 负值（修复后） | length=-3, width=5 | `Rect(-3,5)` | 抛出 IllegalArgumentException |

#### getArea() 边界值分析

| 边界 | 测试用例 | 预期结果 |
|------|---------|---------|
| 最小正值 | `Rect(1,1).getArea()` | 1 |
| 大值 | `Rect(10000,10000).getArea()` | 100000000 |
| 溢出边界 | `Rect(50000,50000).getArea()` | 整数溢出（已知限制） |

#### getPerimeter() 等价类划分

| 等价类 | 输入描述 | 测试用例 | 预期结果 |
|--------|---------|---------|---------|
| 正常正值 | length=5, width=10 | `Rect(5,10).getPerimeter()` | 30 |
| 正方形 | length=width=7 | `Rect(7,7).getPerimeter()` | 28 |
| 单位值 | length=1, width=1 | `Rect(1,1).getPerimeter()` | 4 |
| 零值 | length=0, width=5 | `Rect(0,5).getPerimeter()` | 10 |
| 全零 | length=0, width=0 | `Rect(0,0).getPerimeter()` | 0 |

#### findMax() 等价类划分

| 等价类 | 输入描述 | 预期结果 |
|--------|---------|---------|
| 正常数组 + 面积比较 | 3个不同面积矩形 | 返回面积最大的矩形 |
| 正常数组 + 周长比较 | 3个不同周长矩形 | 返回周长最大的矩形 |
| 单元素数组 | 1个矩形 | 返回该矩形 |
| 全相等数组 | 3个相同矩形 | 返回其中之一 |
| 最大值在开头 | max在arr[0] | 正确返回 |
| 最大值在末尾 | max在arr[n-1] | 正确返回 |
| 最大值在中间 | max在arr[1] | 正确返回 |
| 空数组（修复后） | `{}` | 抛出 IllegalArgumentException |
| null数组（修复后） | `null` | 抛出 IllegalArgumentException |
| null比较器（修复后） | comparator=null | 抛出 IllegalArgumentException |

### 2.3 黑盒测试 — 基于组合优化（Combinatorial Testing）

对 `getArea()` 和 `getPerimeter()` 使用 **全组合测试**：

- **因子**: length 和 width
- **取值**: {0, 1, 5, 100}
- **测试用例数**: 4 x 4 = 16 组组合

```java
int[] values = {0, 1, 5, 100};
for (int l : values) {
    for (int w : values) {
        Rect r = new Rect(l, w);
        assertEquals(l * w, r.getArea());
        assertEquals(2*l + 2*w, r.getPerimeter());
    }
}
```

所有 16 组组合均通过测试，验证了公式的正确性。

### 2.4 白盒测试（White-box Testing）

#### getArea() 和 getPerimeter()

这两个方法各只有一条 return 语句，无分支结构：
- **语句覆盖率**: 任意一个测试用例即可达到 100%
- **分支覆盖率**: 无分支，不适用

#### findMax() 控制流分析

```
findMax() 控制流:
  maxIndex = 0                          // S1
  for i = 1 to arr.length-1:           // 循环条件 C1
    if cmp.compare(arr[i], arr[maxIndex]) > 0:  // 分支 B1
      maxIndex = i                      // S2 (true分支)
    // else: 不更新                     // (false分支)
  return arr[maxIndex]                  // S3
```

| 覆盖标准 | 测试用例 | 覆盖情况 |
|---------|---------|---------|
| 语句覆盖 | 升序数组 [1,4,9] | S1, S2, S3 全部覆盖 |
| 分支覆盖 | 升序 [1,4,9] + 降序 [9,4,1] | B1 的 true 和 false 分支均覆盖 |
| 路径覆盖 | 单元素、双元素、升序、降序 | 覆盖循环不执行、执行一次、多次的路径 |

### 2.5 单元测试与集成测试

#### 单元测试
- `getArea()`: 14 个测试用例
- `getPerimeter()`: 10 个测试用例
- `findMax()`: 15 个测试用例
- `Comparator`（areaCompare / perimeterCompare）: 6 个测试用例

#### 集成测试
- 模拟 `main()` 方法的完整场景，验证 `findMax` + `areaCompare` + `getObject()` 协同工作
- 测试 setter 修改后 `getArea()` / `getPerimeter()` 是否正确更新
- 验证 `getObject()` 输出格式

## 3. 测试结果

### 修复前（第一轮测试）

```
Tests run: 47, Failures: 1, Errors: 0
```

- 46 个通过，1 个失败（`testFindMax_nullComparator`）
- 发现整数溢出 bug（`[BUG] getArea() integer overflow`）
- 负数输入未被拒绝（产生负面积/周长）

### 修复后（第二轮测试）

```
Tests run: 49, Failures: 0, Errors: 0
```

全部 49 个测试用例通过。

## 4. 发现的 Bug 及修复

### Bug 1: 缺少输入校验（构造函数和 setter）

**问题**: 构造函数和 setter 接受负数，导致面积和周长计算出语义错误的结果。

**修复前**:
```java
public Rect(int length, int width) {
    this.length = length;
    this.width = width;
}
```

**修复后**:
```java
public Rect(int length, int width) {
    if (length < 0 || width < 0) {
        throw new IllegalArgumentException("Length and width must be non-negative");
    }
    this.length = length;
    this.width = width;
}
```

setter 方法同样添加了非负校验。

### Bug 2: findMax() 缺少空数组和 null 检查

**问题**: 传入 null 数组时抛出 `NullPointerException`，传入空数组时抛出 `ArrayIndexOutOfBoundsException`，传入 null 比较器时抛出 `NullPointerException`。这些异常信息不明确，不利于调试。

**修复前**:
```java
public static <AnyType>
AnyType findMax(AnyType[] arr, Comparator<? super AnyType> cmp) {
    int maxIndex = 0;
    for(int i = 1; i < arr.length; i++)
        if(cmp.compare(arr[i], arr[maxIndex]) > 0)
            maxIndex = i;
    return arr[maxIndex];
}
```

**修复后**:
```java
public static <AnyType>
AnyType findMax(AnyType[] arr, Comparator<? super AnyType> cmp) {
    if (arr == null || arr.length == 0) {
        throw new IllegalArgumentException("Array must not be null or empty");
    }
    if (cmp == null) {
        throw new IllegalArgumentException("Comparator must not be null");
    }
    int maxIndex = 0;
    for(int i = 1; i < arr.length; i++)
        if(cmp.compare(arr[i], arr[maxIndex]) > 0)
            maxIndex = i;
    return arr[maxIndex];
}
```

### Bug 3: 源文件编码问题

**问题**: `Rect.java` 中 `main` 方法的中文字符串使用 GBK 编码，在 UTF-8 环境下编译报错。

**修复**: 将中文字符串替换为英文（`"Max Area: "` 和 `"Max Perimeter: "`），统一为 UTF-8 编码。

### 已知限制: 整数溢出

`getArea()` 返回 `int` 类型，当 `length * width` 超过 `Integer.MAX_VALUE`（2,147,483,647）时会发生整数溢出。例如 `Rect(50000, 50000)` 的面积应为 2,500,000,000，但实际返回 -1,794,967,296。如需支持大面积，可将返回类型改为 `long`。

## 5. 测试代码结构

```
src/
├── main/java/
│   └── Rect.java          # 被测源代码
└── test/java/
    └── RectTest.java       # JUnit 测试类（49 个测试用例）
```

### 运行测试

```bash
mvn test
```

## 6. 总结

| 测试类型 | 用例数 | 通过 | 发现缺陷 |
|---------|--------|------|---------|
| 静态测试 | - | - | 5 个问题 |
| 黑盒-等价类划分 | 25 | 25 | 输入校验缺失 |
| 黑盒-边界值分析 | 5 | 5 | 整数溢出 |
| 黑盒-组合测试 | 2（含32子用例） | 2 | 无 |
| 白盒-语句/分支覆盖 | 5 | 5 | 无 |
| 集成测试 | 5 | 5 | 无 |
| Comparator 单元测试 | 6 | 6 | 无 |
| **合计** | **49** | **49** | **3 个 Bug 已修复** |

通过多种测试方法的综合运用，共发现并修复了 3 个 Bug，验证了修复后程序的正确性。
