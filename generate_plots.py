import os
import pandas as pd
import matplotlib.pyplot as plt

os.makedirs('results/plots', exist_ok=True)

df = pd.read_csv('results/results.csv')
df.columns = df.columns.str.strip()

workloads = ['W1', 'W2', 'W3_head', 'W3_middle', 'W4']

metrics = [
    ('time_ms', 'Median time (ms)'),
    ('steps', 'steps (count)'),
    ('moves', 'moves (count)'),
    ('comparisons', 'comparisons (count)')
]

for wl in df['workload'].unique():
    sub_wl = df[df['workload'] == wl]

    variants = sub_wl['variant'].unique()
    for var in variants:
        if var != '-':
            sub = sub_wl[sub_wl['variant'] == var]
            title_prefix = f"{wl} ({var})"
            filename = f"{wl.lower()}_{var}_all_metrics.png"
        else:
            sub = sub_wl
            title_prefix = f"{wl}"
            filename = f"{wl.lower()}_all_metrics.png"

        fig, axes = plt.subplots(2, 2, figsize=(14, 8))
        axes = axes.flatten()

        for idx, (metric, ylabel) in enumerate(metrics):
            ax = axes[idx]
            has_data = False

            for struct in sub['structure'].unique():
                data_struct = sub[sub['structure'] == struct].sort_values('n')

                if (data_struct[metric] > 0).any():
                    has_data = True
                    ax.plot(
                        data_struct['n'],
                        data_struct[metric],
                        marker='o',
                        linewidth=2,
                        label=struct
                    )

            ax.set_xscale('log')
            ax.set_xlabel('n (elements)')
            ax.set_ylabel(ylabel)
            ax.grid(True, which="both", ls="--", alpha=0.5)

            if has_data:
                if metric in ['time_ms', 'steps'] or (sub[metric] > 0).all():
                    ax.set_yscale('log')
                ax.legend()
            else:
                ax.text(0.5, 0.5, 'All counts = 0', horizontalalignment='center',
                        verticalalignment='center', transform=ax.transAxes, fontsize=12)
                ax.set_ylim(-0.1, 1.1)

        fig.suptitle(f"{title_prefix} - Performance & Metrics Analysis", fontsize=14, fontweight='bold')
        plt.tight_layout()

        filepath = os.path.join('results/plots', filename)
        plt.savefig(filepath, dpi=300)
        plt.close()
        print(f"Generated: {filepath}")