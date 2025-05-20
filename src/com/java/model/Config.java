package com.java.model;

import com.java.exception.InvalidConfigurationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Config {
    public static Board loadConfig(String filename)
      throws InvalidConfigurationException
    {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] dims = br.readLine().trim().split("\\s+");
            int A = Integer.parseInt(dims[0]);        // rows
            int B = Integer.parseInt(dims[1]);        // cols
            int declaredCount = Integer.parseInt(br.readLine().trim());

            Board board = new Board(A, B);

            List<String> lines = new ArrayList<>();
            String ln;
            while ((ln = br.readLine()) != null) {
                lines.add(ln);
            }

            int gridRowsSeen = 0;
            boolean exitFound = false;

            for (String line : lines) {
                int xIdx = line.indexOf('K');

                if (!exitFound
                    && gridRowsSeen < A
                    && xIdx >= 0
                    && line.length() == B + 1
                    && (xIdx == 0 || xIdx == B))
                {
                    int row = gridRowsSeen;
                    int col = xIdx == 0 ? -1 : B;
                    board.setExit(row, col);
                    if (xIdx == 0) {
                        for (int c = 0; c < B; c++) {
                            board.parseCell(row, c, line.charAt(c + 1));
                        }
                    } else {
                        for (int c = 0; c < B; c++) {
                            board.parseCell(row, c, line.charAt(c));
                        }
                    }
                    gridRowsSeen++;
                    exitFound = true;

                } else if (xIdx >= 0 && line.trim().equals("K") && !exitFound) {
                    int row = gridRowsSeen == 0 ? -1 : A;
                    board.setExit(row, line.indexOf('K'));
                    exitFound = true;

                } else if (gridRowsSeen < A) {
                    if (line.length() < B) {
                        throw new InvalidConfigurationException(
                          "Grid row " + gridRowsSeen + " must have ≥ " + B + " chars"
                        );
                    }
                    for (int c = 0; c < B; c++) {
                        board.parseCell(gridRowsSeen, c, line.charAt(c));
                    }
                    gridRowsSeen++;
                }
            }

            if (gridRowsSeen < A) {
                throw new InvalidConfigurationException(
                  "Expected " + A + " grid rows, but only found " + gridRowsSeen
                );
            }
            if (!exitFound) {
                throw new InvalidConfigurationException("No exit 'K' found in configuration");
            }

            Map<Character, Piece> pcs = board.getPieces();
            if (!pcs.containsKey('P')) {
                throw new InvalidConfigurationException("No primary piece 'P' found");
            }
            if (pcs.size() - 1 != declaredCount) {
                throw new InvalidConfigurationException(
                  "Declared piece count mismatch: expected "
                  + declaredCount + " non-primary pieces, found "
                  + (pcs.size() - 1)
                );
            }

            int er = board.getExitRow(), ec = board.getExitCol();
            boolean exitLeft   = ec < 0;
            boolean exitRight  = ec > B - 1;
            boolean exitTop    = er < 0;
            boolean exitBottom = er > A - 1;

            for (Piece p : pcs.values()) {
                if (p.getSize() == 1) {
                    p.setHorizontal(exitLeft || exitRight);
                }
            }

            return board;

        } catch (IOException e) {
            throw new InvalidConfigurationException("Error reading configuration: " + e.getMessage());
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("Invalid number format: " + e.getMessage());
        }
    }
}
