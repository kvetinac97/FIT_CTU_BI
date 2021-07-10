//
//  TableDetailViewModel.swift
//  RestSys
//
//  Created by Thats Me on 21.01.2021.
//

import Foundation
import FirebaseDatabase
import PDFKit

final class TableDetailViewModel: ViewModelRead<FoodOrder> {
    
    private var databaseReference: DatabaseReference!
    
    init () {
        databaseReference = Database.database().reference(withPath: "kitchen")
    }

    func loadFood (table: Table) {
        super.loadEntities(url: "/order-food/at/" + String(table.id))
    }

    func trashOrder (orderId: Int, table: Table, dismiss: @escaping () -> ()) {
        networkService.delete(url: "/orders/" + String(orderId)) { [weak self, dismiss] result in
            switch result {
                case .success:
                    DispatchQueue.main.async {
                        dismiss()
                    }
                case let .failure(error):
                    self?.displayError(error)
            }
        }
        databaseReference.child("Table \(table.id)").removeValue()
    }
    
    func submitOrder (orderId: Int, table: Table, dismiss: @escaping () -> ()) {
        networkService.patch(url: "/orders/" + String(orderId), body: ["table": nil, "datetime": nil, "completed": true]) { [weak self, dismiss] result in
            switch result {
                case .success:
                    DispatchQueue.main.async {
                        dismiss()
                    }
                case let .failure(error):
                    self?.displayError(error)
            }
        }
        databaseReference.child("Table \(table.id)").removeValue()
    }
    
}
