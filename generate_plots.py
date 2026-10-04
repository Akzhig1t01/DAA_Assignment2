import os
import pandas as pd
import matplotlib.pyplot as plt

csv_path = 'results/results.csv'
if not os.path.exists(csv_path):
    print("Error: results/results.csv not found!")
    exit()

df = pd.read_csv(csv_path)
df.columns = df.columns.str.strip().str.lower()

os.makedirs('results/plots', exist_ok=True)

w1 = df[df['workload'].astype(str).str.upper() == 'W1']
if not w1.empty:
    plt.figure(figsize=(8, 5))
    for struct in w1['structure'].unique():
        sub = w1[w1['structure'] == struct].sort_values('n')
        plt.plot(sub['n'], sub['time_ms'], marker='o', linestyle='-', linewidth=2, label=struct)
    if len(w1['n'].unique()) > 1:
        plt.xscale('log')
    plt.title('W1: Random Access Time vs n')
    plt.xlabel('n')
    plt.ylabel('Time (ms)')
    plt.legend()
    plt.grid(True, which="both", ls="--")
    plt.savefig('results/plots/w1_time.png', dpi=300)
    plt.close()

w2 = df[df['workload'].astype(str).str.upper() == 'W2']
if not w2.empty:
    plt.figure(figsize=(8, 5))
    for struct in w2['structure'].unique():
        sub = w2[w2['structure'] == struct].sort_values('n')
        plt.plot(sub['n'], sub['comparisons'], marker='s', linestyle='-', linewidth=2, label=struct)
    if len(w2['n'].unique()) > 1:
        plt.xscale('log')
    plt.title('W2: Search Comparisons vs n')
    plt.xlabel('n')
    plt.ylabel('Comparisons')
    plt.legend()
    plt.grid(True, which="both", ls="--")
    plt.savefig('results/plots/w2_comparisons.png', dpi=300)
    plt.close()

w3 = df[df['workload'].astype(str).str.upper() == 'W3']
if not w3.empty:
    plt.figure(figsize=(8, 5))
    for key, grp in w3.groupby(['structure', 'variant']):
        grp_sorted = grp.sort_values('n')
        plt.plot(grp_sorted['n'], grp_sorted['time_ms'], marker='^', linestyle='-', linewidth=2, label=f"{key[0]} ({key[1]})")
    if len(w3['n'].unique()) > 1:
        plt.xscale('log')
    plt.title('W3: Insert & Remove Time vs n')
    plt.xlabel('n')
    plt.ylabel('Time (ms)')
    plt.legend()
    plt.grid(True, which="both", ls="--")
    plt.savefig('results/plots/w3_time.png', dpi=300)
    plt.close()

w4 = df[df['workload'].astype(str).str.upper() == 'W4']
if not w4.empty:
    plt.figure(figsize=(8, 5))
    w4_sorted = w4.sort_values('n')
    plt.plot(w4_sorted['n'], w4_sorted['time_ms'], marker='o', linestyle='-', linewidth=2, color='green', label='MinHeap')
    if len(w4['n'].unique()) > 1:
        plt.xscale('log')
    plt.title('W4: MinHeap Time vs n')
    plt.xlabel('n')
    plt.ylabel('Time (ms)')
    plt.legend()
    plt.grid(True, which="both", ls="--")
    plt.savefig('results/plots/w4_time.png', dpi=300)
    plt.close()

print("Plots successfully generated in results/plots/")