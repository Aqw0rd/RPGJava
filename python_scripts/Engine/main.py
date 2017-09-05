import pygame
import pygame.locals
import Tile as tile
import Player, Bat
import KeyInput
import random


t = []
p = Player.Player(200, 100)
bat = []
for x in range(0, 50):
    bat.append(Bat.Bat(random.randint(0,640),random.randint(0,480)))


def generate_tiles(w,h, list):
    for x in range(0, w):
        tx = []
        for y in range(0, h):
            tx.append(tile.Tile())
            tx[y].pos = [x*32,y*32]
            tx[y].col = [255,255,255]
        list.append(tx)

def load_tileset(filename, width, height, scale):
    image = pygame.image.load(filename).convert_alpha()
    image = pygame.transform.scale(image, (int(image.get_width()*scale), int(image.get_height()*scale)))
    image_width = image.get_width()
    image_height = image.get_height()
    tile_table = []
    for tile_x in range(0, image_width // int(width * scale)):
        line = []
        tile_table.append(line)
        for tile_y in range(0, image_height // int(height * scale)):
            rect = (tile_x * int(width * scale), tile_y * int(height * scale), int(width * scale), int(height * scale))
            line.append(image.subsurface(rect))
    #print(len(tile_table))
    return tile_table

if __name__ == '__main__':
    pygame.init()
    width = 640
    height = 480
    clock = pygame.time.Clock()
    playtime = 0.0
    FPS = 60
    gameloop = True
    time = 0
    screen = pygame.display.set_mode((width, height))

    #############LOAD IMAGE TILESET FOR GAMEOBJECT #############

    p.img = load_tileset('link.jpg', 64, 96, 0.5)
    for x in bat:
        x.img = load_tileset('bat.png', 32,32,1.5)


    ############################################################
    #bat.running = True
    p.up, p.down, p.left, p.right = 2,3,0,1
    for x in bat:
        x.up, x.down, x.left, x.right = 2, 0, 3, 1
    keys = [False, False, False, False]         #List of which movement key is pressed

    generate_tiles(width//32,height // 32, t)
    while gameloop:
        milliseconds = clock.tick(FPS)
        playtime += milliseconds / 1000.0

        # Print framerate and playtime in titlebar.
        text = "FPS: {0:.2f}   Playtime: {1:.2f}".format(clock.get_fps(), playtime)
        pygame.display.set_caption(text)

        #Drawing all the tiles
        for i in t:
            for j in i:
                pygame.draw.rect(screen, (j.col[0], j.col[1], j.col[2]), (j.pos[0], j.pos[1], 32, 32))

        ################### DRAW THE SCREEN ELEMENTS #################

        screen.blit(p.img[p.mov_animation][p.orientation], p.pos)
        for x in bat:
            screen.blit(x.img[x.mov_animation][x.orientation], x.pos)

        ############################################################

        #update the screen
        pygame.display.flip()
        # loop through the events
        for event in pygame.event.get():
            # check if the event is the X button
            if event.type == pygame.QUIT:
                # if it is quit the game
                pygame.quit()
                exit(0)

            KeyInput.KeyCheck(event,keys) #Key events

        ######## CALL UPDATE FUNCTION FOR GAMEOBJECTS ########

        p.update()
        p.movement(keys)
        for x in bat:
            x.update(p)
            x.object_orientation()
            x.time += milliseconds
            x.anim(130)


        ######################################################

        p.object_orientation()


        p.time +=milliseconds
        p.anim(80)

        #object_animation(bat, 130)