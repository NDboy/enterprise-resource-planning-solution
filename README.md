Vizsgaremek – ERP App (Enterprise Resource Planning)

Részletek:
___
Az alkalmazás egy vállalatirányítási rendszer izolált része, mely a szállító számlák (A/P invoice) könyvelését végzi, az ehhez minimálisan szükséges kapcsolódó osztályokkal, úgymint a szállító (Partner), a könyvelést végző személy (Employee) és maga a könyvelési aktus (Accounting). Az applikáció REST webszolgáltatáson keresztül elérhető, a perzisztálás egy Docker konténerben futtatott, 3307-es porton kapcsolódó MariaDB-vel történt. Az alkalmazás a jövőbeni tovább fejlesztés reményében készült.
Az entitások és a hozzájuk kapcsolódó funkciók rövid bemutatása:

APInvoice (szállító számla):
- String id (saját generálású „beszédes”, egyedi azonosító E21- prefix-szel)
- String invNum (a szállító által adott számla azonosító)
- PaymentModeAndDates paymentModeAndDates (beágyazott/Embedded, gyűjtő attribútum – kiállítás dátuma, esedékesség, fizetési mód, bruttó érték)
- InvoiceStatus invoiceStatus (Enum: OPEN, PAYED, CANCELED – nyitott, kifizetett és sztornózott állapot)
- List<InvoiceItem> invoiceItems (@ElementCollection - számlatételek: nettó-, bruttó összeggel, ÁFA mértékkel és ÁFA összeggel)
- Partner partner (@ManyToOne - Szállító - kapcsolati attribútum)
- Employee employee (Könyvelő személye – kapcsolati attribútum)
- List<Accounting> accountings (Könyvelési aktusok – kapcsolati attribútum)

Végpontok:

|Http metódus  | Végpont                        | Részletek                                                                          | 
|--------------|--------------------------------|------------------------------------------------------------------------------------|
|POST          |"/api/apinvoices"               | rögzíthetjük a számlát                                                             |
|GET           |"/api/apinvoices"               | kilistázhatjuk és szűrhetjük a számlákat                                           |
|GET           |"/api/apinvoices/{id}"          | lekérhetünk azonosító alapján egy számlát                                          |
|PUT           |"/api/apinvoices/{id}/partner"  | az id alapján lekért számlának beállíthatjuk a partner attribútumát                |
|PUT           |"/api/apinvoices/{id}/employee" | az id alapján lekért számlának beállíthatjuk az employee attribútumát              |
|PUT           |"/api/apinvoices/{id}"          | az id alapján lekért számlának megváltoztathatjuk az invoiceStatus attribútumát    |
|DELETE        |"/api/apinvoices/{id}"          | az id alapján lekért számlát törölhetjük (éles környezetben ezt nem implementálnám)|

Partner (szállító/vevő):
- String id (saját generálású „beszédes”, egyedi azonosító P- prefix-szel)
- String name (szállító/vevő elnevezése)
- Address address (beágyazott/Embedded, gyűjtő attribútum - ország, irányítószám, címsor)
- String taxNo (adószám)
- Set<String> ibans (@ElementCollection - bankszámla számok)
- List<APInvoice> apInvoices (@OneToMany - a partner által kiállított számlák, kapcsolati attribútum)

Végpontok:

|Http metódus  | Végpont                   | Részletek                                                                            | 
|--------------|---------------------------|--------------------------------------------------------------------------------------|
|POST          |"/api/partners"            | rögzíthetjük a Partner-t                                                             |
|GET           |"/api/partners"            | kilistázhatjuk és szűrhetjük a Partner-eket                                          |
|GET           |"/api/partners/{id}"       | lekérhetünk azonosító alapján egy Partner-t                                          |
|PUT           |"/api/partners/{id}/ibans" | az id alapján lekért Partner-hez új bankszámla számot adhatunk                       |
|DELETE        |"/api/partners/{id}"       | az id alapján lekért Partner-t törölhetjük (éles környezetben ezt nem implementálnám)|

Employee (könyvelést végző alkalmazott):
- String id (saját generálású „beszédes”, egyedi azonosító a keresztnév első- valamint a vezetéknév első kettő karakterének összekonkatenálásával)
- String firstName (keresztnév)
- String lastName (vezetéknév)
- EmployeeStatus status (Enum: ACTIVE, PASSIVE, QUIT)
- Address address (beágyazott/Embedded, gyűjtő attribútum - ország, irányítószám, címsor)
- LocalDate entryDate (belépés dátuma)

Végpontok:

|Http metódus  | Végpont                      | Részletek                                                            | 
|--------------|------------------------------|----------------------------------------------------------------------|
|POST          |"/api/employees"              | rögzíthetjük az Employee-t                                           |
|GET           |"/api/employees"              | kilistázhatjuk és szűrhetjük az alkalmazottakat                      |
|GET           |"/api/employees/{id}"         | lekérhetünk azonosító alapján egy alkalmazottat                      |
|PUT           |"/api/employees/{id}/status"  | az id alapján lekért alkalmazottnak megváltoztathatjuk a státuszát   |
|PUT           |"/api/employees/{id}/address" | az id alapján lekért alkalmazottnak megváltoztathatjuk a címét       |

Accounting (könyvelési aktus, a szállító számla komplett, partnerrel, alkalmazottal való bekönyvelésekor, valamint a szállító számla státuszának módosításakor jön létre):
- String id (saját generálású „beszédes”, egyedi azonosító "ACC21-" prefix-szel)
- LocalDate accountingDate (könyvelés dátuma)
- Employee employee (könyvelést végző, kapcsolati attribútum)
- InvoiceStatus invoiceStatus (Enum: OPEN, PAYED, CANCELED)
- APInvoice apInvoice (szállító számla, @ManyToOne/ kapcsolati attribútum)


Végpontok:

|Http metódus  | Végpont                 | Részletek                                      | 
|--------------|-------------------------|------------------------------------------------|
|GET           |"/api/accountings"       | kilistázhatjuk és szűrhetjük a könyveléseket   |
|GET           |"/api/accountings/{id}"  | lekérhetünk azonosító alapján egy könyvelést   |

