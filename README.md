# SusGameBackend

## Working with DTO

### Setup
```
REPO_PATH=$(git rev-parse --show-toplevel)
cd "$REPO_PATH/.."
git clone https://github.com/Nepommuck/SusGameDTO.git
ln -s "$REPO_PATH/../SusGameDTO/src/main/kotlin" "$REPO_PATH/external"
cd "$REPO_PATH"
```

### Update to newest version
```
REPO_PATH=$(git rev-parse --show-toplevel)
cd "$REPO_PATH/../SusGameDTO"
git pull
cd "$REPO_PATH"
```

### Modify
In order to modify DTO, commit to [**SusGameDTO** repository](https://github.com/Nepommuck/SusGameDTO)
