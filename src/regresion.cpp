#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <map>
#include <limits>
#include <algorithm>
#include <cmath>  

using namespace std;


struct Producto {
    string nombre;
    vector<double> precios;
};


void leerArchivo(const string &archivo, map<string, Producto> &productos) {
    ifstream file(archivo);
    string line;

    if (!file.is_open()) {
        cerr << "Error al abrir el archivo: " << archivo << endl;
        return;
    }

    while (getline(file, line)) {
        stringstream ss(line);
        string nombreProducto;
        string precioStr;
        double precio;

        if (!getline(ss, nombreProducto, ',')) continue;
        if (!getline(ss, precioStr, ',')) continue;

        try {
            precio = stod(precioStr);
        } catch (const invalid_argument &) {
            cerr << "Error: Precio inválido en el producto " << nombreProducto << endl;
            continue;
        }

        productos[nombreProducto].nombre = nombreProducto;
        productos[nombreProducto].precios.push_back(precio);
    }

    file.close();
}


double predecirPrecio(const vector<double> &precios, int cantidad) {
    double theta0 = 0;
    double theta1 = 0;
    double alpha = 0.001;  
    double lambda = 0.1;   
    int iteraciones = 1000;
    int m = precios.size();

    if (m == 0) {
        cerr << "Error: No hay precios para predecir." << endl;
        return NAN;  
    }


    vector<double> cantidades(m);
    for (int i = 0; i < m; i++) {
        cantidades[i] = i + 1;
    }


    double maxPrecio = *max_element(precios.begin(), precios.end());
    double maxCantidad = *max_element(cantidades.begin(), cantidades.end());

    vector<double> scaledPrecios(m);
    vector<double> scaledCantidades(m);
    for (int i = 0; i < m; i++) {
        scaledPrecios[i] = precios[i] / maxPrecio;
        scaledCantidades[i] = cantidades[i] / maxCantidad;
    }


    for (int i = 0; i < iteraciones; i++) {
        double errorSum0 = 0;
        double errorSum1 = 0;

        for (int j = 0; j < m; j++) {
            double prediccion = theta0 + theta1 * scaledCantidades[j];
            double error = prediccion - scaledPrecios[j];
            errorSum0 += error;
            errorSum1 += error * scaledCantidades[j];
        }

        theta0 -= (alpha / m) * (errorSum0 + lambda * theta0);
        theta1 -= (alpha / m) * (errorSum1 + lambda * theta1);


        if (isnan(theta0) || isnan(theta1)) {
            cerr << "Error: Se produjo NaN en la iteración " << i << endl;
            return NAN;
        }
    }

    double prediccionEscalada = theta0 + theta1 * (cantidad / maxCantidad);
    return prediccionEscalada * maxPrecio;
}

int main() {
    map<string, Producto> productos;
    string archivoTXT = "C++/productos_y_precios.txt";
    leerArchivo(archivoTXT, productos);

    cout << "Productos disponibles:\n";
    for (const auto &entry : productos) {
        cout << entry.first << endl;
    }

    string seleccionProducto;
    cout << "Seleccione el nombre del producto para hacer la predicción: ";
    cin >> seleccionProducto;

    if (productos.find(seleccionProducto) == productos.end()) {
        cerr << "Producto no válido!" << endl;
        return -1;
    }

    int cantidad;
    cout << "Ingrese la cantidad para la predicción: ";
    while (true) {
        cin >> cantidad;
        if (cin.fail() || cantidad < 1) {
            cout << "Por favor, ingrese un número entero positivo para la cantidad: ";
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
        } else {
            break;
        }
    }

    Producto productoSeleccionado = productos[seleccionProducto];
    double precioPredicho = predecirPrecio(productoSeleccionado.precios, cantidad);


    if (isnan(precioPredicho)) {
        cerr << "Error en la predicción del precio." << endl;
        return -1;
    }

    ofstream resultado("C++/resultado_prediccion.txt");
    if (resultado.is_open()) {
        resultado << "Predicted price for " << cantidad << " units of " 
                  << productoSeleccionado.nombre << ": " << precioPredicho << endl;
        resultado.close();
    } else {
        cerr << "Error al escribir en el archivo de resultados!" << endl;
    }

    return 0;
}