import pygame.event


def KeyCheck(e,keys):
    if e.type == pygame.KEYDOWN:
        if e.key == pygame.K_w:
            keys[0] = True
        elif e.key == pygame.K_a:
            keys[1] = True
        elif e.key == pygame.K_s:
            keys[2] = True
        elif e.key == pygame.K_d:
            keys[3] = True
    if e.type == pygame.KEYUP:
        if e.key == pygame.K_w:
            keys[0] = False
        elif e.key == pygame.K_a:
            keys[1] = False
        elif e.key == pygame.K_s:
            keys[2] = False
        elif e.key == pygame.K_d:
            keys[3] = False