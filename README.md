# 🛒 Grocery Shopping App 

[Previous sections remain the same until Modules Package...]

## 🔄 Adapters

### 📋 CustomAdapter
- Main product listing adapter
- Features:
  - Displays grocery items in a card layout
  - Real-time search filtering
  - Add to cart functionality with Firebase integration
  - Product details on click
- Components:
  ```java
  - CardView for item display
  - TextView for name and price
  - ImageView for product icon
  - Add to Cart button
  ```
- Key functionalities:
  - Filter products by name
  - Firebase cart management
  - Duplicate product check
  - Error handling and user feedback

### 🛍️ CartAdapter
- Shopping cart management adapter
- Features:
  - Displays cart items with quantities
  - Real-time price calculations
  - Quantity adjustment controls
  - Total price updates
- Components:
  ```java
  - CardView for cart items
  - TextView for name, price, and quantity
  - Plus/Minus buttons for quantity control
  - Total price display
  ```
- Key functionalities:
  - Real-time Firebase synchronization
  - Quantity management
  - Automatic price recalculation
  - Item removal when quantity reaches zero

## 🔥 Firebase Integration
- Real-time Database implementation
- Features:
  - User authentication
  - Cart data persistence
  - Real-time price updates
  - User-specific cart management
- Data Structure:
  ```
  user/
    ├── [phone]/
    │   |
    │   ├── cart/
    │   │   ├── product1/
    │   │   │   ├── name
    │   │   │   ├── price
    │   │   │   ├── amount
    │   │   │   └── id
    │   │   └── product2/
    |   └── email
    |   ├── phone
    │   └── totalPrice
    └── [phone2]/
  ```

## 🔧 Technical Features
- RecyclerView implementations with custom adapters
- Real-time search filtering
- Dynamic price calculations
- Firebase Realtime Database integration
- User authentication
- Error handling and user feedback
- Bundle-based data passing
- Responsive UI with CardView layouts

## 💾 Data Management
- Firebase Realtime Database for data storage
- Local dataset management with ArrayLists
- Real-time data synchronization
- Efficient filtering mechanisms
- Cart state persistence

## 🚀 Getting Started
1. Clone the repository
2. Configure Firebase:
   - Add google-services.json
   - Enable Authentication
   - Set up Realtime Database rules
   - Configure Firebase storage
3. Open in Android Studio
4. Build and run the application

## 📦 Dependencies
```gradle
dependencies {
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'com.google.firebase:firebase-auth:21.0.1'
    implementation 'com.google.firebase:firebase-database:20.0.3'
    implementation 'androidx.navigation:navigation-fragment:2.3.5'
    implementation 'androidx.navigation:navigation-ui:2.3.5'
}
```

## 🔒 Security Features
- Firebase Authentication integration
- User-specific data isolation
- Secure cart management
- Error handling and validation

## 🤝 Contributing
Feel free to submit issues and enhancement requests!

