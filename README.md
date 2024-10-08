Hello, đây là nơi mình ghi lại những gì chúng mình đã code được trong suốt thời gian vừa qua.

1. Tạo GamePanel, Main để hiển thị màn hình game cơ bản

    (Main.java) B1: Tạo Main.java và thiết lập các thông số để hiển thị màn hình
    
    (GamePanel.java) B2: Tạo GamePanel.java để cài đặt các giá trị cơ bản cho tile, màn hình

    (Main.java) B3: Tạo gp ở Main và add vào window

    (GamePanel.java) B4: Tạo Thread để chạy game, imlements Runnable. Lúc này cần override run() method
    run() hiểu là chạy những gì trong nó trong suốt game

    (Main.java) B5: Chạy startGameThread() để chạy tất cả những gì có trong run()

    (GamePanel.java) B6: Tạo update() và paintComponent() để cập nhật obj và vẽ nó
    Nhớ đưa 2 method vào vòng while trog run()

2. Tạo KeyHandler để di chuyển obj bằng phím

    (KeyHandler.java) B1: Tạo file và implement từ KeyListener để nhận tín hiệu bàn phím
    Override các method tương ứng

    (GamePanel.java)
    B2: Tạo keyH và khởi tạo trong constructor
    B3: Cấu hình FPS dùng delta time và nanotime

3. Tạo Entity và Player kế thừa

    Entity là một lớp chung mô tả các đặc điểm và hành vi mà nhiều đối tượng trong trò chơi đều có. Khi obj kế thừa từ Entity,  ta không cần phải viết lại tất cả các thuộc tính và phương thức này cho riêng nó. Nhờ đó ta có thể tận dụng để tiết kiệm thời gian. Có thể dễ dàng mở rộng lớp Entity mà không cần phải sửa đổi từng lớp con. Nếu không có lớp Entity, ta sẽ phải định nghĩa lại các thuộc tính và phương thức chung trong nhiều lớp khác nhau như Player, Enemy, ... Các obj có thể mở rộng hoặc ghi đè (override) những hành vi khác nếu cần
    
    (Entity.java) B1: Tạo file và thiết lập các thuộc tính và phương thức cơ bản
    
    (Player.java)
    B2: Tạo file nhân vật extends từ Entity và thiết lập thông số và constructor truyền vào gp và keyH. Khởi tạo player bên (GamePanel.java) tương ứng.
    B3: Cài đặt di chuyển cho player. Override các method update(), draw(). Ở (GamePanel.java), thêm update() vào update() và draw() vào draw()

    (Entity.java)
    B3: Viết method ImportEachImage() để tạo 1 vector animation bên trong chứa vài cái vector a lưu các bufferedimage cho từng vector a
    B4: Viết các method rectGet(), rectDraw(), setDefaultValue(), detectMoveAndDraw()

    (Player.java) B5: Ở constructor của Player cần thêm setDefaultValue() và getPlayerImage(). Ở phần update, cấu hình để trạng thái nhân vật thay đổi theo phím nhận. Làm vòng lặp animation. ở phần draw cần thêm detectMoveAndDraw();

4. Tạo Tile và TileManager

    B1: Tạo Tile.java và TileManager.java

    (TileManager.java) B2: Xây dựng map bằng cách dùng mảng 2 chiều. Mỗi phần tử của mảng tương ứng với 1 ô trong game. Kích cỡ 1 ô đang mặc định là 48x48

    (GamePanel.java) B3: Khai báo biến tileMng, thêm method tileMng.draw() vào draw()

5. Camera và World

    (Player.java) (Entity.java)
    B1: Khai báo thêm các biến screenX, screenY, đổi x, y thành worldX, worldY, khai báo giá trị cho player
    
    (GamePanel.java) (Player.java) (Entity.java)
    B2: Thêm các thông số maxWorldCol, maxWorldRow, worldWidth, worldHeight để định nghĩa cho World. Trong khi đó, ở phần update() của player, chỉnh lại x, y thành worldX, worldY. Ở detectMoveAndDraw() trong Entity, cần đổi lại x, y ở drawImage thành screenX, screenY. Vì đây là các biến final nên sẽ có tác dụng giúp nhân vật luôn ở giữa màn hình


6. Tạo Collision

    (TileManager.java) B1: Trong getTileImage(), thêm collision vào những tile không thể đi xuyên (vd: nước, cây, tường, ...)

    B2: Tạo thư mục collision, add CollisionCheck.java, constructor truyền vào gp. Add method checkTile(). Thêm collisionOn vào Entity 

    (GamePanel.java) (Player.java)
    B3: Ở GamePanel, khởi tạo CollisionCheck cCheck. Ở Player update(), thêm collisionOn và method checkTile()

    (Player.java) B4: Chỉnh lại điều kiện di chuyển của player trong update()

7. Tạo object, đặt nó trên bản đồ, xử lí va chạm 2 object

    (Slime.java) B1: Tạo Slime.java extends từ Entity. Set constructor gần tương tự như Player như rectGet(), rectDraw();
    getSlimeImage(). Tạm thời phần update() chỉ có hàm Lặp lại hoạt hình. Phần draw cũng có drawObjImage() như Player.

    (GamePanel.java) B2: Khởi tạo 1 arrayList<Entity> obj. Ở update() thì update() các animation của object. Ở draw() cũng có method draw()

    (AssetSetter.java) B3: Tạo 1 file mới, file này có chức năng đọc file txt có cấu trúc (VD: Slime 2 20,20 25,25) Sau đó sẽ khởi tạo các vật trên vị trí tương ứng - method setObject(path)
    
    (GamePanel.java) (Main.java) B4: Khởi tạo aSetter, tạo 1 method setupGame() có nhiệm vụ chạy aSetter.setObject(path). Ở main cũng khởi tạo gp.setupGame()

    (CollisionCheck.java) B5: Tại đây, viết method checkObject()

8. Viết effect, đạn bắn, cập nhật Collision cho đạn

    Tạo MouseHandler.java để quản lý input chuột. Tại GamePanel, tạo attr mouseX, mouseY để keep track chuyển động chuột. Tạo method onClick(). Tạo MouseListener trong construtor.

    Tạo Effect.java kế thừa từ Entity.
    
    Tạo Bullet.java để xử lí đạn
    
    (GamePanel): Tạo int reloadTime để xử lí lặp lại bắn đạn. Thêm vào phần update() để cập nhật

9. Ném bình


