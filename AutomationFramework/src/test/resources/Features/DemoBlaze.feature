@RegressionTest 
Feature: Purchase Laptop 
	Description: The purpose of this feature file is to test purchase functionality

Scenario Outline: PurchaseLaptop 
	Given Open Browser 
	When Add Laptops "<ProductsToAdd>" to Cart
	And Delete Products "<ProductsToDelete>" from Cart
	And Place Order 
	And Complete Web Form with "<Name>", "<Country>", "<City>", "<Credit Card>", "<Month>" and "<Year>" 
	And Purchase Order
	Then Verify Product is Purchased 
	And Capture Product Info 
	And Click OK to Confirm Purchase
	
	Examples: 
		| ProductsToAdd            | ProductsToDelete | Name    | Country | City  | Credit Card | Month | Year |
		| Sony vaio i5;Dell i7 8gb | Dell i7 8gb      | Pradeep | India   | Delhi | 123456789   | March | 2020 |