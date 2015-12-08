# Calculator
```math
|         A or B|
|:-------------:|
|     C     | ÷ |
| 7 | 8 | 9 | × |
| 4 | 5 | 6 | - |
| 1 | 2 | 3 | + |
|   0   | . | = |
```
| レジスタ | 用途         | 初期値 |
|:--------:|:------------:|:-------|
| A        | 数字を保存   | 0      |
| B        | 数字を保存   | 0      |
| Ope      | 演算子を保存 | *      |
| Frag     | フラグを保存 | false  |


|   State    | Input  | Output                 | Next     |
|:----------:|:-------|:-----------------------|:---------|
|            | 数字   | レジスタAに数字を登録  | ONE_NUM  |
|  `EMPTY`   | 演算子 | 演算子を登録           | ONE_OPE  |
|            | =      | 何もしない             | ANSWER   |
|            | .      | レジスタAに0.を登録    | ONE_NUM  |
|------------|--------|------------------------|----------|
|            | 数字   | レジスタAに数字を追加  | ONE_NUM  |
| `ONE_NUM`  | 演算子 | 演算子を登録           | ONE_OPE  |
|            | =      | 何もしない             | ANSWER   |
|            | .      | レジスタAに.を追加     | ONE_NUM  |
|------------|--------|------------------------|----------|
|            | 数字   | レジスタBに数字を登録  | TWO_NUM  |
| `ONE_OPE`  | 演算子 | 演算子を修正           | ONE_OPE  |
|            | =      | A = A o A              | ANSWER   |
|            | .      | レジスタBに0.を登録    | TWO_NUM  |
|------------|--------|------------------------|----------|
|            | 数字   | レジスタBに数字を追加  | TOW_NUM  |
| `TWO_NUM`  | 演算子 | A = A o B;演算子を登録 | ONE_OPE  |
|            | =      | A = A o B              | ANSWER   |
|            | .      | レジスタBに.を追加     | TWO_NUM  |
|------------|--------|------------------------|----------|
|            | 数字   | レジスタAに数字を登録  | TEMP_NUM |
| `ANSWER`   | 演算子 | 演算子を登録           | ONE_OPE  |
|            | =      | A = A o B              | ANSWER   |
|            | .      | レジスタAに0.を登録    | TEMP_NUM |
|------------|--------|------------------------|----------|
|            | 数字   | レジスタAに数字を追加  | TEMP_NUM |
| `TEMP_NUM` | 演算子 | 演算子を登録           | ONE_OPE  |
|            | =      | A = A o B              | ANSWER   |
|            | .      | レジスタAに.を追加     | TEMP_NUM |
|------------|--------|------------------------|----------|

* 0で割る
* OverFlow