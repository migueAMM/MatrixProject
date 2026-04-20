import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import glob
import os

def plot_latest_results():
    # Buscamos en 'resultados/' directamente porque Java lo llama desde la raíz
    # Pero por si acaso, probamos ambas rutas (raíz y nivel superior)
    archivos = glob.glob('resultados/resultados_*.csv') + glob.glob('../resultados/resultados_*.csv')

    if not archivos:
        print("No se encontraron archivos CSV.")
        print(f"Directorio actual: {os.getcwd()}")
        return

    # Seleccionar el más reciente
    ultimo_archivo = max(archivos, key=os.path.getctime)
    print(f" Graficando: {ultimo_archivo}")

    # on_bad_lines='skip' hace que ignore las filas con errores de comas
    df = pd.read_csv(ultimo_archivo, on_bad_lines='skip')

    plt.figure(figsize=(12, 7))
    sns.set_theme(style="whitegrid")

    grafica = sns.barplot(x='Tiempo_ms', y='Algoritmo', data=df, palette='viridis', hue='Algoritmo', legend=False)

    plt.title(f'Comparativa de Rendimiento - Matrices {df["Tamano"].iloc[0]}', fontsize=15, fontweight='bold')
    plt.xlabel('Tiempo de Ejecución (milisegundos)', fontsize=12)
    plt.ylabel('Algoritmo', fontsize=12)

    for p in grafica.patches:
        width = p.get_width()
        plt.text(width + (width * 0.01), p.get_y() + p.get_height()/2. + 0.1,
                 f'{int(width)} ms', va='center', fontsize=10)

    plt.tight_layout()

    # Guardar imagen en la misma carpeta del CSV
    nombre_imagen = ultimo_archivo.replace('.csv', '.png')
    plt.savefig(nombre_imagen)

    # MOSTRAR GRÁFICA
    plt.show()

if __name__ == "__main__":
    plot_latest_results()